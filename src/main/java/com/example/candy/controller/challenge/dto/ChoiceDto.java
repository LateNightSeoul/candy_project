package com.example.candy.controller.challenge.dto;

import com.example.candy.domain.choice.Choice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChoiceDto {

    @ApiModelProperty
    private int seq;

    @ApiModelProperty
    private String content;

}
