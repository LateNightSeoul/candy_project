package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CandyCancelRequestDto {
    @ApiModelProperty
    private Long challengeId;
    @ApiModelProperty(value = "부모 비밀번호")
    private String parentPassword;
    public CandyCancelRequestDto() {}
    public CandyCancelRequestDto(Long challengeId, String parentPassword) {
        this.challengeId = challengeId;
        this.parentPassword = parentPassword;
    }
}
