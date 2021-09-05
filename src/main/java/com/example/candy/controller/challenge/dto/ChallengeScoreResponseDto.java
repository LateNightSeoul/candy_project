package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeScoreResponseDto {
    @ApiModelProperty(value = "역대 스코어 중 가장 높은 점수")
    public int highestScoreOfAllTime;

    public ChallengeScoreResponseDto() {}
    public ChallengeScoreResponseDto(int highestScoreOfAllTime) { this.highestScoreOfAllTime = highestScoreOfAllTime; }
}
