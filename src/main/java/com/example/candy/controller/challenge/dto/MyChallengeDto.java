package com.example.candy.controller.challenge.dto;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MyChallengeDto {

    @ApiModelProperty(value = "챌린지 id값", example = "1")
    private Long challengeId;
    @ApiModelProperty(value = "강의 id값", example = "1")
    private Long lectureId;
    @ApiModelProperty(value = "과목명(KOREAN, ENGLISH, MATH)" ,example = "KOREAN")
    private Category category;
    @ApiModelProperty(value = "제목", example = "5형식")
    private String title;
    @ApiModelProperty(value = "부제목", example = "5형식 동사에 대한 이해")
    private String subTitle;
    @ApiModelProperty(value = "챌린지 상세 설명", example = "5형식 동사의 다양한 예시를 통해 이해를 높입니다.")
    private String description;
    @ApiModelProperty(value = "총점수", example = "100")
    private int totalScore;
    @ApiModelProperty(value = "캔디를 받기위한 커트라인", example = "80")
    private int requiredScore;
    @ApiModelProperty(value = "챌린지에 배정된 캔디", example = "10")
    private int assignedCandy;
    @ApiModelProperty(value = "완료 여부", example = "true")
    private boolean complete;
    @ApiModelProperty(value = "챌린지 level", example = "2")
    private int level;

    public MyChallengeDto(Long challengeId, Long lectureId, Category category, String title, String subTitle,
                          int totalScore, String description, int requiredScore, int assignedCandy ,boolean complete, int level) {
        this.challengeId = challengeId;
        this.lectureId = lectureId;
        this.category = category;
        this.title = title;
        this.subTitle = subTitle;
        this.totalScore = totalScore;
        this.description = description;
        this.requiredScore = requiredScore;
        this.assignedCandy = assignedCandy;
        this.complete = complete;
        this.level = level;
    }
}
