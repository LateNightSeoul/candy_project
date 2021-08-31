package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProblemSolvedRequestDto {

	@ApiModelProperty(value = "challenge id 값", example = "1")
    private Long challengeId;
    @ApiModelProperty(value = "문제 id 값", example = "1")
    private Long problemId;
    @ApiModelProperty(value = "문제 점수", example = "50")
    private Long problemScore;

}
