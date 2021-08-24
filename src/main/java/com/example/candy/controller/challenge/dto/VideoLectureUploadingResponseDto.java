package com.example.candy.controller.challenge.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoLectureUploadingResponseDto {
	@ApiModelProperty(value = "강의 url")
    private String videoUrl;
}
