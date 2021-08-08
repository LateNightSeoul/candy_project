package com.example.candy.controller.candyHistory;

import com.example.candy.domain.candy.EventType;
import lombok.Getter;

@Getter
public class CandyWithdrawRequestDto {
    private final int amount;
    private final EventType eventType;

    public CandyWithdrawRequestDto(int amount) {
        this.amount = amount;
        this.eventType = EventType.WITHDRAW;
    }
}
