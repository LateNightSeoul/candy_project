package com.example.candy.controller.candyHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CandyAttainRequestDto {
    @ApiModelProperty
    private final Long challengeId;
}
