package com.example.candy.controller.challenge;

import com.example.candy.controller.ApiResult;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.choice.ChoiceService;
import com.example.candy.service.lecture.LectureService;
import com.example.candy.service.problem.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

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
}