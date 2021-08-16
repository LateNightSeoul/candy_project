package com.example.candy.controller.authentication;

import com.example.candy.controller.ApiResult;
import com.example.candy.controller.authentication.dto.AuthenticationResponseDto;
import com.example.candy.security.AuthenticationRequest;
import com.example.candy.security.AuthenticationResult;
import com.example.candy.security.JwtAuthenticationToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.candy.controller.ApiResult.OK;

@RestController
@RequestMapping("/auth")
@Api(tags = {"로그인"})
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    @ApiOperation(value = "로그인")
    public ApiResult<AuthenticationResponseDto> authentication(@ApiParam @RequestBody AuthenticationRequest authRequest) {
        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return OK(
                    new AuthenticationResponseDto((AuthenticationResult) authentication.getDetails())
            );
        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
