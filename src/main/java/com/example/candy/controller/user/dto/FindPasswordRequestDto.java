package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class FindPasswordRequestDto {
    @ApiModelProperty
    private String email;

    public FindPasswordRequestDto(){}
    public FindPasswordRequestDto(String email) {this.email = email;}
}
