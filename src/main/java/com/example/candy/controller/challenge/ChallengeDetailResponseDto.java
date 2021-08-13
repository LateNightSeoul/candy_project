package com.example.candy.controller.challenge;

import com.example.candy.enums.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class ChallengeDetailResponseDto {

    private String title;

    private String subTitle;

    private int assignedCandy = 0;

    private boolean likeDone = false;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private int totalScore;

    private int requiredScore;

    private int level;

    private int problemCount;

    public ChallengeDetailResponseDto(String title, String subTitle, Object assignedCandy, Object id, Category category, String description,
                                      int totalScore, int requiredScore, int level, int problemCount) {
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
