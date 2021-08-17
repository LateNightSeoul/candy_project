package com.example.candy.controller.candyHistory.dto;

import com.example.candy.domain.candy.EventType;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
public class CandyHistoryResponseDto {
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private LocalDateTime createDate;
    private int amount;

    public CandyHistoryResponseDto() {}

    public CandyHistoryResponseDto(EventType eventType, LocalDateTime createDate, int amount) {
        this.eventType = eventType;
        this.createDate = createDate;
        this.amount = amount;
    }
}
