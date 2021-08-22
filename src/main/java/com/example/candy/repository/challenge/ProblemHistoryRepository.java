package com.example.candy.repository.challenge;

import com.example.candy.domain.problem.ProblemHistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemHistoryRepository extends JpaRepository<ProblemHistory, Long> {
	Optional<ProblemHistory> findByProblem_id(Long problemId);
	Optional<ProblemHistory> findByChallengeHistory_idAndProblem_id(Long challengeHistoryId, Long problemId);

}
