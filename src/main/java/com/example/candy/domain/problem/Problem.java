package com.example.candy.domain.problem;

import com.example.candy.controller.challenge.dto.ProblemDto;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.choice.Choice;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<Choice> choices;

    private int seq;

    private String question;

    private String content;

    private int score;

    private boolean isMultiple;

    private String answer;

    private int multipleAnswer;

    private int multipleCount;

    private LocalDateTime modifiedDate;

    public static Problem create(ProblemDto problemDto) {
        return Problem.builder()
                .seq(problemDto.getSeq())
                .question(problemDto.getQuestion())
                .content(problemDto.getContent())
                .isMultiple(problemDto.isMultiple())
                .answer(problemDto.getAnswer())
                .multipleAnswer(problemDto.getMultipleAnswer())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public void addChoice(Choice choice) {
        if (this.choices == null) {
            this.choices = new ArrayList<>();
        }
        this.choices.add(choice);
        choice.setProblem(this);
    }

}
