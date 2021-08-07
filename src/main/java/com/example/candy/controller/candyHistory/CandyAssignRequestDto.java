package com.example.candy.controller.candyHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CandyAssignRequestDto {
    private final long challengeId;
    private final int candyAmount;
}
