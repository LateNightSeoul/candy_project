package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class NewParentPasswordRequestDto {
	@ApiModelProperty(value = "이메일", example = "test@gmail.com")
    private String email;
	@ApiModelProperty(value = "2차 비밀번호", example = "1234")
    private String parentPassword;

    public NewParentPasswordRequestDto(){}
    public NewParentPasswordRequestDto(String email, String parent_password) {this.email = email; this.parentPassword = parentPassword;}
}
