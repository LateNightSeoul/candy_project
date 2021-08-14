package com.example.candy.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class FindEmailRequestDto {
    @ApiModelProperty
    private String name;

    public FindEmailRequestDto(){}
    public FindEmailRequestDto(String name) {this.name = name;}
}
