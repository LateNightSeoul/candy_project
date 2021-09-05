package com.example.candy.controller.challenge.dto;

import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChallengeDto {

    @ApiModelProperty(value = "challenge id값", example = "1")
    private Long id;
    @ApiModelProperty(value = "과목명(KOREAN, ENGLISH, MATH)" ,example = "KOREAN")
    private Category category;
    @ApiModelProperty(value = "제목", example = "5형식")
    private String title;
    @ApiModelProperty(value = "부제목", example = "5형식 동사에 대한 이해")
    private String subTitle;
    @ApiModelProperty(value = "좋아요 여부", example = "false")
    private boolean likeDone = false;
    @ApiModelProperty(value = "총점수", example = "100")
    private int totalScore;
    @ApiModelProperty(value = "캔디를 받기위한 커트라인", example = "80")
    private int requiredScore;
    @ApiModelProperty(value = "강의 id", example = "1")
    private Long lectureId;
    @ApiModelProperty(value = "챌린지 level", example = "2")
    private int level;

    public ChallengeDto(Long id, Category category, String title, String subTitle, Object likeDone,
                        int totalScore, int requiredScore, Long lectureId, int level) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.subTitle = subTitle;
        if (likeDone != null) {
            this.likeDone= true;
        }
        this.totalScore = totalScore;
        this.requiredScore = requiredScore;
        this.lectureId = lectureId;
        this.level = level;
    }
}
