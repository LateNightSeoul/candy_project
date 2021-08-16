package com.example.candy.controller.challenge.dto;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class ChallengeRegisterResponseDto {

    @ApiModelProperty(value = "제목", example = "5형식")
    private String challengeTitle;
    @ApiModelProperty(value = "챌린지에 있는 문제의 개수", example = "3")
    private int problemCount;
    @ApiModelProperty(value = "과목명(KOREAN, ENGLISH, MATH)" ,example = "KOREAN")
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
