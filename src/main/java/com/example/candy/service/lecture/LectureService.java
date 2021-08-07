package com.example.candy.service.lecture;

import com.example.candy.domain.lecture.Lecture;
import com.example.candy.repository.lecture.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectureService {

    @Autowired
    LectureRepository lectureRepository;

    private Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

}
