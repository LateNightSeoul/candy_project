package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemMarkingRSDto {

	@ApiModelProperty(value = "정답인지/아닌지", example = "O/X")
    private String answerMark;
	
	public ProblemMarkingRSDto(String answerMark) {
		this.answerMark = answerMark;
	}

}
