package com.example.candy.controller.challenge.dto;

import com.example.candy.enums.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChallengeDto {
    private Long id;
    private Category category;
    private String title;
    private String subTitle;
    private boolean likeDone = false;
    private int totalScore;
    private int requiredScore;

    public ChallengeDto(Long id, Category category, String title, String subTitle, Object likeDone, int totalScore, int requiredScore) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.subTitle = subTitle;
        if (likeDone != null) {
            this.likeDone= true;
        }
        this.totalScore = totalScore;
        this.requiredScore = requiredScore;
    }
}
