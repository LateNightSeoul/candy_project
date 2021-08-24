package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ChangePasswordResponseDto {
    @ApiModelProperty(value = "userId", example = "1")
    private Long userId;

    public ChangePasswordResponseDto() {}
    public ChangePasswordResponseDto(Long userId) { this.userId = userId; }
}
