package com.example.candy.controller.candyHistory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@NoArgsConstructor
public class CandyWithdrawResponseDto {

    @ApiModelProperty(value = "남은 학생 캔디 갯수", example = "10")
    private int candyAmount;
    @ApiModelProperty(value = "인출 된 캔디 갯수", example = "50")
    private int withdrewCandy;

    public CandyWithdrawResponseDto(int candyAmount, int withdrewCandy) {
        this.candyAmount = candyAmount;
        this.withdrewCandy = withdrewCandy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("withdrewCandy", withdrewCandy)
                .append("candyAmount", candyAmount)
                .toString();
    }


}
