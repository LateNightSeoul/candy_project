package com.example.candy.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ValidateEmailRequestDto {
    @ApiModelProperty
    private String email;
    @ApiModelProperty
    private String auth;

    public ValidateEmailRequestDto(){}
    public ValidateEmailRequestDto(String email, String auth) {this.email = email; this.auth = auth;}
}
