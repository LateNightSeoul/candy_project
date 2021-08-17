package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CandyAssignRequestDto {
    @ApiModelProperty
    private final Long challengeId;
    @ApiModelProperty(value = "배정할 캔디 갯수", example = "13")
    private final int candyAmount;
}
