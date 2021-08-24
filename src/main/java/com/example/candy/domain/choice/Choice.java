package com.example.candy.domain.choice;

import com.example.candy.controller.challenge.dto.ChoiceDto;
import com.example.candy.domain.problem.Problem;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Choice {

    @Id @GeneratedValue
    @Column(name = "choice_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private int seq;

    private String content;

    public static Choice create(ChoiceDto choiceDto) {
        return builder()
                .seq(choiceDto.getSeq())
                .content(choiceDto.getContent())
                .build();
    }
}
