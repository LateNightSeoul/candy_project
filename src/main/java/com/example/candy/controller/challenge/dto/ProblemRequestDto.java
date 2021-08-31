package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemRequestDto {

	@ApiModelProperty(value = "challenge id 값", example = "1")
    private Long challengeId;

}
