package com.example.candy.controller.challenge.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ProblemMarkingResponseDto {
	@ApiModelProperty(value = "채점결과 리스트")
    private List<ProblemMarkingRSDto> problemMarkingRSDto;
	
	public ProblemMarkingResponseDto(List<ProblemMarkingRSDto> problemMarkingRSDto) {
		this.problemMarkingRSDto = problemMarkingRSDto;
	}
   
}
