package com.example.candy.controller.candyHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter

public class CandyCancelRequestDto {
    private Long challengeId;
    public CandyCancelRequestDto() {}
    public CandyCancelRequestDto(Long challengeId) {this.challengeId = challengeId;}
}
