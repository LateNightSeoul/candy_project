package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemSolvedRequestDtoList {
    @ApiModelProperty(value = "challenge id 값", example = "1")
    private Long challengeId;
	@ApiModelProperty(value = "푼 문제 리스트")
    private List<ProblemSolvedRequestDto> problemSolvedRequestDto;

	public ProblemSolvedRequestDtoList() {}
    public ProblemSolvedRequestDtoList(Long challengeId, List<ProblemSolvedRequestDto> problemSolvedRequestDto) {
        this.challengeId = challengeId;
        this.problemSolvedRequestDto = problemSolvedRequestDto;
    }
}
