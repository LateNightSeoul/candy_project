package com.example.candy.controller.challenge;

import com.example.candy.controller.ApiResult;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.security.JwtAuthentication;
import com.example.candy.service.challenge.ChallengeLikeService;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.choice.ChoiceService;
import com.example.candy.service.lecture.LectureService;
import com.example.candy.service.problem.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ChallengeLikeService challengeLikeService;

    @PostMapping("/register")
    public ApiResult<ChallengeRegisterResponseDto> register(@RequestBody ChallengeRegisterRequestDto challengeRegisterRequestDto) {

        Challenge challenge = Challenge.builder()
                .title(challengeRegisterRequestDto.getTitle())
                .subTitle(challengeRegisterRequestDto.getSubTitle())
                .category(challengeRegisterRequestDto.getCategory())
                .description(challengeRegisterRequestDto.getDescription())
                .totalScore(challengeRegisterRequestDto.getTotalScore())
                .requiredScore(challengeRegisterRequestDto.getRequiredScore())
                .level(challengeRegisterRequestDto.getLevel())
                .problemCount(challengeRegisterRequestDto.getProblemCount())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        List<LectureDto> lectureDtoList = challengeRegisterRequestDto.getLectureDtoList();

        for (LectureDto lectureDto : lectureDtoList) {
            Lecture lecture = Lecture.builder()
                    .videoUrl(lectureDto.getVideoUrl())
                    .content(lectureDto.getContent())
                    .fileUrl(lectureDto.getFileUrl())
                    .build();

            challenge.addLecture(lecture);
        }

        List<ProblemDto> problemDtoList = challengeRegisterRequestDto.getProblemDtoList();

        for (ProblemDto problemDto : problemDtoList) {
            Problem problem = Problem.builder()
                    .seq(problemDto.getSeq())
                    .content(problemDto.getContent())
                    .isMultiple(problemDto.isMultiple())
                    .answer(problemDto.getAnswer())
                    .multipleAnswer(problemDto.getMultipleAnswer())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            challenge.addProblem(problem);

            List<ChoiceDto> choiceDtoList = problemDto.getChoiceDtoList();
            for (ChoiceDto choiceDto : choiceDtoList) {
                Choice choice = Choice.builder()
                        .seq(choiceDto.getSeq())
                        .content(choiceDto.getContent())
                        .build();
                problem.addChoice(choice);
            }
        }

        Challenge findChallenge = challengeService.registerChallenge(challenge);

        return ApiResult.OK(new ChallengeRegisterResponseDto(findChallenge));
    }


    /*
    @GetMapping("/all")
    public ApiResult<List<ChallengeDto>> challenges(@AuthenticationPrincipal JwtAuthentication authentication) {
        List<ChallengeDto> challengeDtoList = new ArrayList<>();
        List<Challenge> challengeList = challengeService.findAllChallenges();
        List<ChallengeLike> challengeLikes = challengeLikeService.findChallengeLikes(authentication.id);
        for (ChallengeLike challengeLike : challengeLikes) {
            challengeLike.getChallenge().getId();

        }

    }

     */


    @PostMapping("{challengeId}/like")
    public ApiResult<Long> like(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable Long challengeId
    ) {
        ChallengeLike saved = challengeLikeService.like(authentication.id, challengeId);
        return ApiResult.OK(saved.getId());
    }

    @GetMapping("likeList")
    public ApiResult<List<ChallengeDto>> likeList(
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<ChallengeDto> challengeDtoList = new ArrayList<>();
        List<ChallengeLike> challengeLikeList = challengeLikeService.findAll(authentication.id);
        for (ChallengeLike challengeLike : challengeLikeList) {
            Challenge challenge = challengeLike.getChallenge();
            ChallengeDto challengeDto = new ChallengeDto(challenge.getId(), challenge.getCategory(), challenge.getTitle(),
                    challenge.getSubTitle(),true,challenge.getTotalScore(),challenge.getRequiredScore());
            challengeDtoList.add(challengeDto);
        }
        return ApiResult.OK(challengeDtoList);
    }
}