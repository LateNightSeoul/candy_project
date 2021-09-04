package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ProblemSolvingResponseDto {
    @ApiModelProperty(value = "userId", example = "1")
    private Long userId;
    @ApiModelProperty(value = "현재 solve에서 획득한 점수", example = "70")
    private int totalScore;

    public ProblemSolvingResponseDto() {}

    public ProblemSolvingResponseDto(Long userId, int totalScore) {
        this.userId = userId;
        this.totalScore = totalScore;
    }
}
