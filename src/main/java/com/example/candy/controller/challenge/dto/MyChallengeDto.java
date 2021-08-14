package com.example.candy.controller.challenge.dto;

import com.example.candy.enums.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyChallengeDto {

    private Long id;
    private Category category;
    private String title;
    private String subTitle;
    private int totalScore;
    private int requiredScore;
    private int assignedCandy;
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
