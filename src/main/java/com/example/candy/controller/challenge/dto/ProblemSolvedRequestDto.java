package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class ProblemSolvedRequestDto {
    @ApiModelProperty(value = "문제 id 값", example = "1")
    private Long problemId;
    @ApiModelProperty(value = "문제 점수", example = "50")
    private Long problemScore;

    public ProblemSolvedRequestDto() {}
    public ProblemSolvedRequestDto(Long problemId, Long problemScore) {
        this.problemId = problemId;
        this.problemScore = problemScore;
    }
}
