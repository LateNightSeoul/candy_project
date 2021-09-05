package com.example.candy.controller.challenge.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponseDto {

	@ApiModelProperty(value = "보기 등록")
    private List<ChoiceDto> choiceDtoList;

	@ApiModelProperty(value = "problemId", example = "proble/solve 시 해당 정보를 꼭 맞게 입력해야 합니다.")
	private Long problemId;

    @ApiModelProperty(value = "문제 번호", example = "1")
    private int seq;

    @ApiModelProperty(value = "문제 질문", example = "빈칸에 들어갈 알맞은 단어는?")
    private String question;

    @ApiModelProperty(value = "문제 내용", example = "one has much to put into them, a day has a hundred pockets.")
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

    @ApiModelProperty(value = "바뀐 날짜")
    private LocalDateTime modifiedDate;

}
