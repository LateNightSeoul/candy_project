package com.example.candy.controller.candyHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CandyCancelRequestDto {
    @ApiModelProperty
    private Long challengeId;
    public CandyCancelRequestDto() {}
    public CandyCancelRequestDto(Long challengeId) {this.challengeId = challengeId;}
}
