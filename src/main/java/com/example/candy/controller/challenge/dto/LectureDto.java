package com.example.candy.controller.challenge.dto;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.lecture.Lecture;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
public class LectureDto {

    @ApiModelProperty(value = "강의 url")
    private String videoUrl;
    @ApiModelProperty(value = "강의 설명", example = "5형식 동사의 A~C 까지 다루는 강의입니다.")
    private String content;
    @ApiModelProperty(value = "첨부파일(강의 자료) URL")
    private String fileUrl;

}
