package com.example.candy.controller.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.problem.Problem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
