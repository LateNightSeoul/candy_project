package com.example.candy.controller.candyHistory;

import com.example.candy.domain.candy.EventType;
import lombok.Getter;

@Getter
public class CandyChargeRequestDto {
    private final int amount;
    private final EventType eventType;

    CandyChargeRequestDto(int amount) {
        this.amount = amount;
        this.eventType = EventType.CHARGE;
    }
}
