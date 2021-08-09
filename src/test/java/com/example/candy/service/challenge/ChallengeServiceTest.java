package com.example.candy.service.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.user.User;
import com.example.candy.repository.challenge.ChallengeDtoRepository;
import com.example.candy.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @BeforeAll
    void setUp() {
        email = "test@gmail.com";
        password = "1234";
        parentPassword = "abcd";
        name = "한글";
        phone = "01012345678";
        birth = "19950302";
    }

    @Test
    @Transactional
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

    @Test
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
        User user = userService.join(email, true, password, parentPassword, name, phone, birth);
        ChallengeLike challengeLike = challengeLikeService.like(user.getId(), challenge.getId());
        Optional<ChallengeLike> findOne = challengeLikeService.findChallengeLike(user.getId(), savedChallenge.getId());

        //then
        Assertions.assertEquals(challengeLike,findOne.get());

        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).getTitle() = " + challengeDtoRepository.findChallenges(user.getId()).get(0).getTitle());
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).getSubTitle() = " + challengeDtoRepository.findChallenges(user.getId()).get(0).getSubTitle());
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).getTotalScore() = " + challengeDtoRepository.findChallenges(user.getId()).get(0).getTotalScore());
        System.out.println("challengeDtoRepository.findChallenges(user.getId()).get(0).isLike() = " + challengeDtoRepository.findChallenges(user.getId()).get(0).isLike());


    }


}
