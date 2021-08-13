package com.example.candy.controller.challenge;

import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChallengeRegisterRequestDto {

    @ApiModelProperty
    private String title;

    @ApiModelProperty
    private String subTitle;

    @ApiModelProperty
    @Enumerated(EnumType.STRING)
    private Category category;

    @ApiModelProperty
    private String description;

    @ApiModelProperty
    private int totalScore;

    @ApiModelProperty
    private int requiredScore;

    @ApiModelProperty
    private int level;

    @ApiModelProperty
    private int problemCount;

    @ApiModelProperty
    private List<ProblemDto> problemDtoList;

    @ApiModelProperty
    private List<LectureDto> lectureDtoList;

}
