package com.example.candy.controller.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.lecture.Lecture;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
public class LectureDto {

    @ApiModelProperty
    private String videoUrl;
    @ApiModelProperty
    private String content;
    @ApiModelProperty
    private String fileUrl;

}
