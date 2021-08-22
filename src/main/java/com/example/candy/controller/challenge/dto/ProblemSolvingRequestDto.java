package com.example.candy.controller.challenge.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ProblemSolvingRequestDto {
	@ApiModelProperty(value = "문제풀이 리스트")
    private List<ProblemSolvingDto> problemSolvingDtoList;
}
