package com.example.candy.controller.candyHistory.dto;

import com.example.candy.domain.candy.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CandyWithdrawRequestDto {
    @ApiModelProperty(value = "수행할 캔디 양")
    private int amount;

    public CandyWithdrawRequestDto(int amount) {
        this.amount = amount;
    }
}
