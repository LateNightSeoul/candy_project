package com.example.candy.controller.challenge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProblemDto {

    @ApiModelProperty(value = "보기 등록")
    private List<ChoiceDto> choiceDtoList;

    @ApiModelProperty(value = "문제 번호", example = "1")
    private int seq;

    @ApiModelProperty(value = "문제 내용", example = "빈칸에 들어갈 알맞은 단어는?")
    private String content;

    @ApiModelProperty(value = "문제에 배정된 점수", example = "10")
    private int score;

    @ApiModelProperty(value = "객관식인지 아닌지. 객관식이면 True", example = "true")
    private boolean isMultiple;

    @ApiModelProperty(value = "객관식 답", example = "3")
    private int multipleAnswer;

    @ApiModelProperty(value = "주관식 답", example = "apple")
    private String answer;

    @ApiModelProperty(value = "보기가 몇개인지(Choice의 갯수) ex) 4지선다", example = "4")
    private int multipleCount;

    @ApiModelProperty(value = "주관식 답", example = "apple")
    private LocalDateTime modifiedDate;

}
