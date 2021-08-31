package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemSolvedRequestDtoList {

	@ApiModelProperty(value = "푼 문제 리스트")
    private List<ProblemSolvedRequestDto> problemSolvedRequestDto;

}
