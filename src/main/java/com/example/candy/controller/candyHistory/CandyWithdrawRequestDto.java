package com.example.candy.controller.candyHistory;

import com.example.candy.domain.candy.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class CandyWithdrawRequestDto {
    @ApiModelProperty(value = "수행할 캔디 양")
    private final int amount;

    public CandyWithdrawRequestDto(int amount) {
        this.amount = amount;
    }
}
