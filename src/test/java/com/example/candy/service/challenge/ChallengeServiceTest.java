package com.example.candy.service.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChallengeServiceTest {
    @Autowired
    ChallengeService challengeService;

    @Test
    public void 챌린지_등록() {
        Challenge challenge = Challenge.builder()
                .title("영어")
                .build();

        Lecture lecture = Lecture.builder()
                .videoUrl("asdsaf")
                .build();

        Problem problem = Problem.builder()
                .content("안녕하세요")
                .build();

        Choice choice = Choice.builder()
                .seq(1)
                .build();

        problem.addChoice(choice);
        challenge.addLecture(lecture);
        challenge.addProblem(problem);

        Challenge saveChallenge = challengeService.registerChallenge(challenge);
        System.out.println("saveChallenge.getLectures().get(0).getVideoUrl() = " + saveChallenge.getLectures().get(0).getVideoUrl());
        System.out.println("saveChallenge.getTitle() = " + saveChallenge.getTitle());
        System.out.println("saveChallenge.getProblems().get(0).getContent() = " + saveChallenge.getProblems().get(0).getContent());
        System.out.println("saveChallenge.getProblems().get(0).getChoices().get(0)..getSeq() = " + saveChallenge.getProblems().get(0).getChoices().get(0).getSeq());

    }


}
