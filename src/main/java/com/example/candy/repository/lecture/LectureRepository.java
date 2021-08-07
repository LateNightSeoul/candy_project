package com.example.candy.repository.lecture;

import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
