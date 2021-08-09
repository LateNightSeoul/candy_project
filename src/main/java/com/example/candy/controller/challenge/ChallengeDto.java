package com.example.candy.controller.challenge;

import com.example.candy.enums.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChallengeDto {
    private Long id;
    private Category category;
    private String title;
    private String subTitle;
    private boolean likeDone;
    private int totalScore;
    private int requiredScore;

    public ChallengeDto(Long id, Category category, String title, String subTitle, boolean likeDone, int totalScore, int requiredScore) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.subTitle = subTitle;
        this.likeDone= likeDone;
        this.totalScore = totalScore;
        this.requiredScore = requiredScore;
    }
}
