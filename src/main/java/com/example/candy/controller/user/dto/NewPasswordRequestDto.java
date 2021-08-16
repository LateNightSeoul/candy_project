package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class NewPasswordRequestDto {
	@ApiModelProperty(value = "이메일", example = "test@gmail.com")
    private String email;
	@ApiModelProperty(value = "비밀번호", example = "test1234")
    private String password;

    public NewPasswordRequestDto(){}
    public NewPasswordRequestDto(String email, String password) {this.email = email; this.password = password;}
}
