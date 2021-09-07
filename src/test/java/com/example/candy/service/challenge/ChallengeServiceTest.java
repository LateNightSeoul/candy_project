package com.example.candy.service.challenge;

import com.example.candy.controller.challenge.dto.ChallengeDetailResponseDto;
import com.example.candy.controller.challenge.dto.ChallengeDto;
import com.example.candy.domain.challenge.Challenge;

import com.example.candy.domain.challenge.ChallengeLike;

import com.example.candy.domain.challenge.ChallengeHistory;

import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.user.User;

import com.example.candy.repository.challenge.ChallengeDtoRepository;
import com.example.candy.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import com.example.candy.repository.user.UserRepository;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChallengeServiceTest {

    @Autowired
    ChallengeService challengeService;
    @Autowired
    ChallengeLikeService challengeLikeService;
    @Autowired
    UserService userService;
    @Autowired
    ChallengeDtoRepository challengeDtoRepository;

    private String email;
    private String password;
    private String parentPassword;
    private String name;
    private String phone;
    private String birth;

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
        System.out.println("saveChallenge.getLectures().get(0).getVideoUrl() = " + challenge.getLecture().getVideoUrl());
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
    public void 챌린지_좋아요() {
        //given
        Challenge challenge = Challenge.builder()
                .title("영어")
                .subTitle("돟사")
                .totalScore(90)
                .requiredScore(50)
                .build();

        //when
        Challenge savedChallenge = challengeService.registerChallenge(challenge);
//        User user = userService.join(email, true, password, parentPassword, name, phone, birth);
        ChallengeLike challengeLike = challengeLikeService.like(user.getId(), challenge.getId());
        Optional<ChallengeLike> findOne = challengeLikeService.findChallengeLike(user.getId(), savedChallenge.getId());
        System.out.println("challengeLike id = " + challengeLike.getId());
        //then
        Assertions.assertEquals(challengeLike,findOne.get());
        List<ChallengeDto> challenges = challengeDtoRepository.findChallenges(user.getId(),10L,10);
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).getTitle() = " + challenges.get(0).getTitle());
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).getSubTitle() = " + challenges.get(0).getSubTitle());
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).getTotalScore() = " + challenges.get(0).getTotalScore());
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).isLike() = " + challenges.get(0).isLikeDone());


    }

    @Order(4)
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

    @Test
    @Order(5)
    @Transactional
    void 챌린지_완료() {

        ChallengeHistory challengeHistory = challengeService.assignCandyInChallengeHistory(challenge.getId(), 30, user);
        challengeService.completeChallenge(challenge.getId(), user.getId());
        assertEquals(challengeHistory.getChallenge().getId(),challenge.getId());
        assertEquals(challengeHistory.isComplete(),true);

    }

    @Test
    @Order(6)
    @Transactional
    void 챌린지_디테일_조회() {
        ChallengeDetailResponseDto challengeDetail = challengeService.findChallengeDetail(user.getId(), challenge.getId());
        assertEquals(challengeDetail.getAssignedCandy(), 0);
        assertEquals(challengeDetail.getTitle(), "영어");
    }

    void 완료한_챌린지_목록() {
        challengeService.assignCandyInChallengeHistory(challenge.getId(), 30, user);
        challengeService.completeChallenge(challenge.getId(), user.getId());
        Long lastChallengeId = 1000L;
        int size = 5;
        List<ChallengeHistory> challengeHistoryList = challengeService.completedChallengeList(user.getId(), true,lastChallengeId,size);
        for (ChallengeHistory ch : challengeHistoryList) {
            assertEquals(ch.isComplete(),true);
        }
    }

    @Test
    @Order(7)
    @Transactional
    void 완료하지_않은_챌린지_목록() {
        challengeService.assignCandyInChallengeHistory(challenge.getId(), 30, user);
        Long lastChallengeId = 1000L;
        int size = 5;
        List<ChallengeHistory> challengeHistoryList = challengeService.notCompletedChallengeList(user.getId(), false,lastChallengeId,size);
        for (ChallengeHistory ch : challengeHistoryList) {
            assertEquals(ch.isComplete(),false);
        }
    }
}
