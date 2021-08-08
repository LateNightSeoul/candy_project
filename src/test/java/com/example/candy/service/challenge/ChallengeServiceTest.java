package com.example.candy.service.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.user.User;
import com.example.candy.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChallengeServiceTest {
    @Autowired ChallengeService challengeService;
    @Autowired UserRepository userRepository;

    private Challenge challenge;
    private Lecture lecture;
    private Problem problem;
    private Choice choice;
    private User user;

    @BeforeAll
    void setUp() {
        Challenge challenge2 = Challenge.builder()
                .title("영어")
                .build();

        lecture = Lecture.builder()
                .videoUrl("asdsaf")
                .build();

        problem = Problem.builder()
                .content("안녕하세요")
                .build();

        choice = Choice.builder()
                .seq(1)
                .build();

        problem.addChoice(choice);
        challenge2.addLecture(lecture);
        challenge2.addProblem(problem);

        challenge = challengeService.registerChallenge(challenge2);

        user = User.builder()
                .email("a@naver.com")
                .password("1234")
                .parentPassword("abcd")
                .name("이해석")
                .birth("19950302")
                .phone("01012345678")
                .build();
        userRepository.save(user);
    }

    @Test
    @Order(1)
    @Transactional
    public void 챌린지_등록() {
        System.out.println("saveChallenge.getLectures().get(0).getVideoUrl() = " + challenge.getLectures().get(0).getVideoUrl());
        System.out.println("saveChallenge.getTitle() = " + challenge.getTitle());
        System.out.println("saveChallenge.getProblems().get(0).getContent() = " + challenge.getProblems().get(0).getContent());
        System.out.println("saveChallenge.getProblems().get(0).getChoices().get(0)..getSeq() = " + challenge.getProblems().get(0).getChoices().get(0).getSeq());
    }

    @Test
    @Order(2)
    @Transactional
    void 챌린지_히스토리_캔디_배정_및_실패(){
        ChallengeHistory challengeHistory = challengeService.assignCandyInChallengeHistory(challenge.getId(), 30, user);
        assertEquals(challengeHistory.getAssignedCandy(), 30);
        assertThrows(IllegalStateException.class, () -> {
            challengeService.assignCandyInChallengeHistory(challenge.getId(), 50, user);
        });
        challengeService.completeChallenge(this.challenge.getId(), user.getId());
        assertThrows(IllegalStateException.class, () -> {
            challengeService.assignCandyInChallengeHistory(challenge.getId(), 50, user);
        });
    }

    @Test
    @Order(3)
    @Transactional
    void 챌린지_히스토리_캔디_배정_및_실패_2() {
        Challenge saveChallenge = challengeService.registerChallenge(challenge);
        challengeService.assignCandyInChallengeHistory(saveChallenge.getId(), 30, user);
        challengeService.cancelCandyAndGetCandyAmount(user.getId(), saveChallenge.getId());
        int amount = challengeService.completeChallenge(saveChallenge.getId(), user.getId());
        assertEquals(challengeService.findChallengeHistoryById(saveChallenge.getId(), user.getId()).get().getAssignedCandy(), 0);
        assertThrows(IllegalStateException.class, () -> {
            challengeService.assignCandyInChallengeHistory(saveChallenge.getId(), 30, user);
        });
    }

}
