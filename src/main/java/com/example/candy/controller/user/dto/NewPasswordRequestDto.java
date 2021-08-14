package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class NewPasswordRequestDto {
    @ApiModelProperty
    private String email;
    @ApiModelProperty
    private String password;

    public NewPasswordRequestDto(){}
    public NewPasswordRequestDto(String email, String password) {this.email = email; this.password = password;}
}
