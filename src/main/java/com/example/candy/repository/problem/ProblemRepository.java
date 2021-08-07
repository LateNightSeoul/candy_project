package com.example.candy.repository.problem;

import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
