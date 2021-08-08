package com.example.candy.controller.candyHistory;

import com.example.candy.domain.candy.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
public class CandyChargeRequestDto {
    private int amount;
    public CandyChargeRequestDto(int amount) {this.amount = amount;}
    public CandyChargeRequestDto() {}
}
