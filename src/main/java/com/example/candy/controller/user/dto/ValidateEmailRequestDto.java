package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ValidateEmailRequestDto {
	@ApiModelProperty(value = "이메일", example = "test@gmail.com")
    private String email;
	@ApiModelProperty(value = "인증코드", example = "000000")
    private String auth;

    public ValidateEmailRequestDto(){}
    public ValidateEmailRequestDto(String email, String auth) {this.email = email; this.auth = auth;}
}
