package com.example.candy.repository.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeLikeRepository extends JpaRepository<ChallengeLike, Long> {
    Optional<ChallengeLike> findByUserAndChallenge(User user, Challenge challenge);

    void deleteByUser_idAndChallenge_id(Long userId, Long challengeId);
    List<ChallengeLike> findAllByUser(User user);
}
