package com.example.candy.controller.user;

import com.example.candy.controller.ApiResult;
import com.example.candy.domain.user.Authority;
import com.example.candy.domain.user.User;
import com.example.candy.security.Jwt;
import com.example.candy.service.email.MailService;
import com.example.candy.service.user.UserService;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Jwt jwt;

    @Autowired
    private UserService userService;

    @PostMapping("/exist")
    public ApiResult<Boolean> checkEmail(@RequestBody Map<String, String> request) {
        return ApiResult.OK(userService.findByEmail(request.get("email")).isPresent());
    }

    @PostMapping("/join")
    public ApiResult<JoinResponseDto> join(@RequestBody JoinRequestDto joinRequestDto) {
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
    public ApiResult<String> findEmail(@RequestBody Map<String, String> request) throws NotFoundException {
    	return ApiResult.OK(userService.find_email(request.get("name")));
    }
    
    @PostMapping("/email")
    public ApiResult<Boolean> sendEmail(@RequestBody Map<String, String> request) throws NotFoundException {
    	return ApiResult.OK(userService.email(request.get("email")));
    }
    
//    @PostMapping("/matches")
//    public ApiResult<Boolean> test_Password(@RequestBody Map<String, String> request) throws NotFoundException {
//    	return ApiResult.OK(userService.password_matches(request.get("email"), request.get("password")));
//    }
    
    @PostMapping("/email/validate")
    public ApiResult<Boolean> validate(@RequestBody Map<String, String> request) throws NotFoundException {
    	return ApiResult.OK(userService.validate(request.get("email"), request.get("auth")));
    }
    
    @PostMapping("/new_pw")
    public ApiResult<Boolean> findPassword(@RequestBody Map<String, String> request) throws NotFoundException {
    	return ApiResult.OK(userService.new_pw(request.get("email"), request.get("password")));
    }
}
