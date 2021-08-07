package com.example.candy.service.problem;

import com.example.candy.domain.problem.Problem;
import com.example.candy.repository.problem.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProblemService {

    @Autowired
    ProblemRepository problemRepository;

    private Problem save(Problem problem) {
        return problemRepository.save(problem);
    }
}
