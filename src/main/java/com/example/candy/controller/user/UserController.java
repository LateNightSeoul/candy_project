package com.example.candy.controller.user;

import com.example.candy.controller.ApiResult;
import com.example.candy.controller.user.dto.*;
import com.example.candy.domain.user.Authority;
import com.example.candy.domain.user.User;
import com.example.candy.security.Jwt;
import com.example.candy.security.JwtAuthentication;
import com.example.candy.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"유저"})
@RequestMapping("/user")
public class UserController {

    @Autowired private Jwt jwt;
    @Autowired private UserService userService;

    @PostMapping("/email/exist")
    @ApiOperation(value = "이메일 중복 확인 (중복일 시 true, 중복이 아닐 시 false 반환)")
    public ApiResult<Boolean> checkEmail(@RequestBody @ApiParam CheckEmailRequestDto checkEmailRequestDto) {
        return ApiResult.OK(userService.findByEmail(checkEmailRequestDto.getEmail()).isPresent());
    }

    @PostMapping("/join")
    @ApiOperation(value = "회원가입")
    public ApiResult<JoinResponseDto> join(@RequestBody @ApiParam JoinRequestDto joinRequestDto) {
        User user = userService.join(
                joinRequestDto.getEmail(), joinRequestDto.isEmailCheck(), joinRequestDto.getPassword(),
                joinRequestDto.getParentPassword(), joinRequestDto.getName(),
                joinRequestDto.getPhone(), joinRequestDto.getBirth()
        );
        String apiToken = user.newApiToken(jwt, new String[]{Authority.STUDENT.value()});
        return ApiResult.OK(
                new JoinResponseDto(apiToken, new UserDto(user))
        );
    }
    
    @PostMapping("/find_email")
    @ApiOperation(value = "이메일 찾기")
    public ApiResult<String> findEmail(@RequestBody @ApiParam FindEmailRequestDto findEmailRequestDto) throws NotFoundException {
    	return ApiResult.OK(userService.find_email(findEmailRequestDto.getName()));
    }
    
    @PostMapping("/email")
    @ApiOperation(value = "비밀번호 찾기를 위한 인증코드 이메일 전송", notes = "항상 true 반환")
    public ApiResult<Boolean> sendEmail(@RequestBody @ApiParam FindPasswordRequestDto findPasswordRequestDto) throws NotFoundException {
    	return ApiResult.OK(userService.email(findPasswordRequestDto.getEmail()));
    }
    
//    @PostMapping("/matches")
//    public ApiResult<Boolean> test_Password(@RequestBody Map<String, String> request) throws NotFoundException {
//    	return ApiResult.OK(userService.password_matches(request.get("email"), request.get("password")));
//    }
    
    @PostMapping("/email/validate")
    @ApiOperation(value = "이메일 인증코드 확인", notes = "인증코드 동일할 시 true, 동일하지 않을 시 false 반환")
    public ApiResult<Boolean> validate(@RequestBody @ApiParam ValidateEmailRequestDto validateEmailRequestDto) throws NotFoundException {
    	return ApiResult.OK(userService.validate(validateEmailRequestDto.getEmail(), validateEmailRequestDto.getAuth()));
    }
    
    @PostMapping("/new_pw")
    @ApiOperation(value = "새로운 비밀번호 설정", notes = "인증했을 시 true, 인증하지 않았을 시 false 반환")
    public ApiResult<Boolean> findPassword(@RequestBody @ApiParam NewPasswordRequestDto newPasswordRequestDto) throws NotFoundException {
    	return ApiResult.OK(userService.new_pw(newPasswordRequestDto.getEmail(), newPasswordRequestDto.getPassword()));
    }

    @GetMapping("/info")
    public ApiResult<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal JwtAuthentication authentication) throws NotFoundException {
        return ApiResult.OK(userService.getUserInfo(authentication.id));
    }
}
