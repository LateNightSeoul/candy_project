package com.example.candy.controller.challenge.dto;

import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyChallengeDto {

    @ApiModelProperty(value = "id값", example = "1")
    private Long id;
    @ApiModelProperty(value = "과목명(KOREAN, ENGLISH, MATH)" ,example = "KOREAN")
    private Category category;
    @ApiModelProperty(value = "제목", example = "5형식")
    private String title;
    @ApiModelProperty(value = "부제목", example = "5형식 동사에 대한 이해")
    private String subTitle;
    @ApiModelProperty(value = "총점수", example = "100")
    private int totalScore;
    @ApiModelProperty(value = "캔디를 받기위한 커트라인", example = "80")
    private int requiredScore;
    @ApiModelProperty(value = "챌린지에 배정된 캔디", example = "10")
    private int assignedCandy;
    @ApiModelProperty(value = "완료 여부", example = "true")
    private boolean complete;

    public MyChallengeDto(Long id, Category category, String title, String subTitle, int totalScore, int requiredScore, int assignedCandy ,boolean complete) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.subTitle = subTitle;
        this.totalScore = totalScore;
        this.requiredScore = requiredScore;
        this.assignedCandy = assignedCandy;
        this.complete = complete;
    }
}
