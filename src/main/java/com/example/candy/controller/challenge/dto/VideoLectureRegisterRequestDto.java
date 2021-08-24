package com.example.candy.controller.challenge.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class VideoLectureRegisterRequestDto {
	@ApiModelProperty(value = "challenge id 값", example = "1")
    private Long challengeId;
}
