package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemSolvingDto {

	@ApiModelProperty(value = "challenge id 값", example = "1")
    private Long challengeId;
    @ApiModelProperty(value = "문제 id 값", example = "1")
    private Long problemId;
    @ApiModelProperty(value = "객관식인지 아닌지. 객관식이면 True", example = "true")
    private boolean isMultiple;
    @ApiModelProperty(value = "객관식 정답", example = "1")
    private int multipleAnswer;
    @ApiModelProperty(value = "주관식 정답", example = "apple")
    private String answer;

}
