package com.example.candy.controller.challenge.dto;

import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
public class ChallengeRegisterRequestDto {

    @ApiModelProperty(value = "제목", example = "5형식")
    private String title;

    @ApiModelProperty(value = "부제목", example = "5형식 동사에 대한 이해")
    private String subTitle;

    @ApiModelProperty(value = "과목명(KOREAN, ENGLISH, MATH)" ,example = "KOREAN")
    @Enumerated(EnumType.STRING)
    private Category category;

    @ApiModelProperty(value = "설명", example = "5형식 전반에 대한 이해를 다루고 있습니다.")
    private String description;

    @ApiModelProperty(value = "총점수", example = "100")
    private int totalScore;

    @ApiModelProperty(value = "캔디를 받기위한 커트라인", example = "80")
    private int requiredScore;

    @ApiModelProperty(value = "챌린지 난이도(예를 들어 범위 0~5)", example = "3")
    private int level;

    @ApiModelProperty(value = "챌린지에 있는 문제의 개수", example = "3")
    private int problemCount;

    @ApiModelProperty(value = "문제 등록")
    private List<ProblemDto> problemDtoList;

    @ApiModelProperty(value = "강의 등록")
    private List<LectureDto> lectureDtoList;

}
