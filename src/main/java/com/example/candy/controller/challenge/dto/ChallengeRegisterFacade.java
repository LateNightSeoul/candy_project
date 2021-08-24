package com.example.candy.controller.challenge.dto;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;

import java.util.List;

public class ChallengeRegisterFacade {
    private ChallengeRegisterRequestDto challengeRegisterRequestDto;

    public ChallengeRegisterFacade(ChallengeRegisterRequestDto challengeRegisterRequestDto) {
        this.challengeRegisterRequestDto = challengeRegisterRequestDto;
    }

    public Challenge getChallenge() {
        Challenge challenge = Challenge.create(challengeRegisterRequestDto);

        List<LectureDto> lectureDtoList = challengeRegisterRequestDto.getLectureDtoList();

        for (LectureDto lectureDto : lectureDtoList) {
            Lecture lecture = Lecture.create(lectureDto);
            challenge.addLecture(lecture);
        }

        List<ProblemDto> problemDtoList = challengeRegisterRequestDto.getProblemDtoList();

        for (ProblemDto problemDto : problemDtoList) {
            Problem problem = Problem.create(problemDto);

            challenge.addProblem(problem);

            List<ChoiceDto> choiceDtoList = problemDto.getChoiceDtoList();
            for (ChoiceDto choiceDto : choiceDtoList) {
                Choice choice = Choice.create(choiceDto);
                problem.addChoice(choice);
            }
        }

        return challenge;
    }

}
