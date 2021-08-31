package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class CandyAssignRequestDto {
    @ApiModelProperty
    private Long challengeId;
    @ApiModelProperty(value = "부모 비밀번호")
    private String parentPassword;
    @ApiModelProperty(value = "배정할 캔디 갯수", example = "13")
    private int candyAmount;

    public CandyAssignRequestDto() {}
    public CandyAssignRequestDto(Long challengeId, String parentPassword, int candyAmount) {
        this.challengeId = challengeId;
        this.parentPassword = parentPassword;
        this.candyAmount = candyAmount;
    }
}
