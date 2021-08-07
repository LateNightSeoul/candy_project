package com.example.candy.controller.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.lecture.Lecture;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
public class LectureDto {

    private String videoUrl;

    private String content;

    private String fileUrl;

}
