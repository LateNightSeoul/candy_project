package com.example.candy.controller.challenge.dto;

import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class ChallengeDetailResponseDto {

    @ApiModelProperty(value = "챌린지Id", example = "1")
    private Long challengeId;
    @ApiModelProperty(value = "제목", example = "5형식")
    private String title;
    @ApiModelProperty(value = "부제목", example = "5형식 동사에 대한 이해")
    private String subTitle;
    @ApiModelProperty(value = "배정된 캔디", example = "10")
    private int assignedCandy = 0;
    @ApiModelProperty(value = "좋아요 여부", example = "false")
    private boolean likeDone = false;

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

    public ChallengeDetailResponseDto(Long challengeId, String title, String subTitle, Object assignedCandy, Object id, Category category, String description,
                                      int totalScore, int requiredScore, int level, int problemCount) {
        this.challengeId = challengeId;
        this.title = title;
        this.subTitle = subTitle;
        if(assignedCandy != null) {
            this.assignedCandy = (int)assignedCandy;
        }

        if (id != null) {
            this.likeDone = true;
        }
        this.category = category;
        this.description = description;
        this.totalScore = totalScore;
        this.requiredScore = requiredScore;
        this.level = level;
        this.problemCount = problemCount;
    }
}
