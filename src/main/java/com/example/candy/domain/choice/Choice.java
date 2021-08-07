package com.example.candy.domain.choice;

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
}
