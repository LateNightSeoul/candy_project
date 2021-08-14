package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CandyAssignRequestDto {
    @ApiModelProperty
    private final long challengeId;
    @ApiModelProperty
    private final int candyAmount;
}
