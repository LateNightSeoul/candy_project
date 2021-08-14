package com.example.candy.controller.candyHistory.dto;

import com.example.candy.domain.candy.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
public class CandyChargeRequestDto {
    @ApiModelProperty(value = "수행할 캔디 양")
    private int amount;
    public CandyChargeRequestDto(int amount) {this.amount = amount;}
    public CandyChargeRequestDto() {}
}
