package com.example.candy.controller.candyHistory.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@RequiredArgsConstructor
public class CandyWithdrawResponseDto {

    private final int withdrewCandy;
    private final int candyAmount;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("withdrewCandy", withdrewCandy)
                .append("candyAmount", candyAmount)
                .toString();
    }


}
