package com.example.candy.controller.challenge.dto;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.enums.Category;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class ChallengeRegisterResponseDto {

    private String challengeTitle;
    private int problemCount;
    private Category category;

    public ChallengeRegisterResponseDto(Challenge challenge) {
        checkArgument(challenge != null, "challenge must be provided.");

        this.challengeTitle = challenge.getTitle();
        this.problemCount = challenge.getProblemCount();
        this.category = challenge.getCategory();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("challengeTitle", challengeTitle)
                .append("problemCount",problemCount)
                .append("category",category)
                .toString();
    }

}
