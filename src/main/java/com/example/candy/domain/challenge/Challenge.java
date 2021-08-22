package com.example.candy.domain.challenge;

import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.enums.Category;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Challenge {

    @Id @GeneratedValue
    @Column(name = "challenge_id")
    private long id;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<Problem> problems;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Lecture> lectures;

    private String title;

    private String subTitle;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private int totalScore;

    private int requiredScore;

    private int level;

    private int problemCount;

    private final LocalDateTime createDate;

    private LocalDateTime modifiedDate;

    public Challenge() {
        this.createDate = LocalDateTime.now();
    }

    public void addProblem(Problem problem) {
        if (this.problems == null) {
            this.problems = new ArrayList<>();
        }
        this.problems.add(problem);
        problem.setChallenge(this);
    }

    public void addLecture(Lecture lecture) {
        if (this.lectures == null) {
            this.lectures = new ArrayList<>();
        }
        this.lectures.add(lecture);
        lecture.setChallenge(this);
    }
}
