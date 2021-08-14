package com.example.candy.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class CheckEmailRequestDto {
    @ApiModelProperty
    private String email;

    public CheckEmailRequestDto(){}
    public CheckEmailRequestDto(String email) {this.email = email;}
}
