package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class FindEmailRequestDto {
    @ApiModelProperty(value = "사용자 이름", example = "김아무개")
    private String name;

    public FindEmailRequestDto(){}
    public FindEmailRequestDto(String name) {this.name = name;}
}
