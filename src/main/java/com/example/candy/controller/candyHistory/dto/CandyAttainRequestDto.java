package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class CandyAttainRequestDto {
    @ApiModelProperty
    private Long challengeId;

    public CandyAttainRequestDto() {}

    public CandyAttainRequestDto(Long challengeId) {
        this.challengeId = challengeId;
    }
}
