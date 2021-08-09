package com.example.candy.service.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.user.User;
import com.example.candy.enums.Category;
import com.example.candy.error.NotFoundException;
import com.example.candy.repository.challenge.ChallengeHistoryRepository;
import com.example.candy.repository.challenge.ChallengeLikeRepository;
import com.example.candy.repository.challenge.ChallengeRepository;
import com.example.candy.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeLikeRepository challengeLikeRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    public ChallengeService(UserRepository userRepository, ChallengeRepository challengeRepository, ChallengeLikeRepository challengeLikeRepository,ChallengeHistoryRepository challengeHistoryRepository) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.challengeLikeRepository = challengeLikeRepository;
        this.challengeHistoryRepository = challengeHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Challenge> findAllChallenges() {
        return challengeRepository.findAll();
    }

    // 필요한 것. user id
    @Transactional(readOnly = true)
    public List<ChallengeHistory> findChallengeHistories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        List<ChallengeHistory> challengeHistories = challengeHistoryRepository.findAllByUser(user);
        return challengeHistories;

    }

    @Transactional(readOnly = true)
    public List<ChallengeHistory> categoryChallengeHistories(Long userId, Category category) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        List<ChallengeHistory> challengeHistory = challengeHistoryRepository.findAllByUserAndCategory(user, category);
        return challengeHistory;
    }

    @Transactional
    public Challenge registerChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }




}
