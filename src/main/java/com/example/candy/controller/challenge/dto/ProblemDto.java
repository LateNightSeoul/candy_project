package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemDto {

    @ApiModelProperty
    private List<ChoiceDto> choiceDtoList;

    @ApiModelProperty
    private int seq;

    @ApiModelProperty
    private String content;

    @ApiModelProperty
    private int score;

    @ApiModelProperty
    private boolean isMultiple;

    @ApiModelProperty
    private String answer;

    @ApiModelProperty
    private int multipleAnswer;

    @ApiModelProperty
    private int multipleCount;

    @ApiModelProperty
    private LocalDateTime modifiedDate;

}
