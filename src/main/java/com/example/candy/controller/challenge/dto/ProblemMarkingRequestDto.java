package com.example.candy.controller.challenge.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ProblemMarkingRequestDto {
	
	@ApiModelProperty(value = "문제채점 리스트")
    private List<ProblemMarkingRQDto> problemMarkingRQDto;

   
}
