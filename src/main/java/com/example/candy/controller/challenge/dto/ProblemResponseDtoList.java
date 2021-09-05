package com.example.candy.controller.challenge.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponseDtoList {

	@ApiModelProperty(value = "문제 리스트")
    private List<ProblemResponseDto> problemResponseDtoList;

}
