package com.example.candy.repository.challenge;

import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.user.User;
import com.example.candy.enums.Category;

import java.util.List;

public interface ChallengeHistoryCustomRepository {
    List<ChallengeHistory> findAllByUser(User user);
    List<ChallengeHistory> findAllByUserAndCategory(User user, Category category);
}
