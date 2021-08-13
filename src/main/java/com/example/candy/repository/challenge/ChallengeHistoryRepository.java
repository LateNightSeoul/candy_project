package com.example.candy.repository.challenge;

import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeHistoryRepository extends JpaRepository<ChallengeHistory, Long>, ChallengeHistoryCustomRepository {
    Optional<ChallengeHistory> findByChallenge_id(Long challengeId);
    Optional<ChallengeHistory> findByChallenge_idAndUser_id(Long challengeId, Long userId);

    List<ChallengeHistory> findAllByUserAndAndComplete(User user, boolean complete);
}
