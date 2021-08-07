package com.example.candy.controller.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.problem.Problem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ProblemDto {

    private List<ChoiceDto> choiceDtoList;

    private int seq;

    private String content;

    private int score;

    private boolean isMultiple;

    private String answer;

    private int multipleAnswer;

    private int multipleCount;

    private LocalDateTime modifiedDate;

    public Problem toEntity() {
        return Problem.builder()
                .seq(seq)
                .content(content)
                .build();
    }

}
