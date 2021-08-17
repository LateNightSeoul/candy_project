package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class CandyResponseDto {
    @ApiModelProperty(value = "요청 수행 뒤 student or parent의 캔디 보유 개수", example = "")
    private int candyAmount;

    public CandyResponseDto() {}
    public CandyResponseDto(int candyAmount) {
        this.candyAmount = candyAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("candyAmount", candyAmount)
                .toString();
    }
}
