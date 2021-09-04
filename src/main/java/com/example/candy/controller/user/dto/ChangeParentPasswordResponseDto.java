package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ChangeParentPasswordResponseDto {
    @ApiModelProperty(value = "userId", example = "1")
    private Long userId;

    public ChangeParentPasswordResponseDto() {}
    public ChangeParentPasswordResponseDto(Long userId) { this.userId = userId; }
}
