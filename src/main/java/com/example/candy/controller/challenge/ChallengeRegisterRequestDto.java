package com.example.candy.controller.challenge;

import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.enums.Category;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChallengeRegisterRequestDto {

    private String title;

    private String subTitle;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private int totalScore;

    private int requiredScore;

    private int level;

    private int problemCount;

    private List<ProblemDto> problemDtoList;

    private List<LectureDto> lectureDtoList;

}
