package com.example.candy.service.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.user.User;
import com.example.candy.enums.Category;
import com.example.candy.error.NotFoundException;
import com.example.candy.repository.challenge.ChallengeHistoryRepository;
import com.example.candy.repository.challenge.ChallengeRepository;
import com.example.candy.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    public ChallengeService(UserRepository userRepository, ChallengeRepository challengeRepository, ChallengeHistoryRepository challengeHistoryRepository) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.challengeHistoryRepository = challengeHistoryRepository;
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

    public void assignCandyInChallengeHistory(Long challengeId, int amount, User user) {
        Optional<ChallengeHistory> findChallengeHistory = challengeHistoryRepository.findByChallenge_id(challengeId);
        ChallengeHistory saveChallengeHistory;
        if (findChallengeHistory.isPresent()) {
            if (findChallengeHistory.get().isComplete() == true || findChallengeHistory.get().getAssignedCandy() != 0) {
                throw new IllegalStateException("ChallengeHistory Already Exists");
            }
            findChallengeHistory.get().setAssignedCandy(amount);
            saveChallengeHistory = findChallengeHistory.get();
        } else {
            Challenge findChallenge = findById(challengeId)
                    .orElseThrow(() -> new NoSuchElementException("No Such Challenge"));
            saveChallengeHistory = new ChallengeHistory(user, findChallenge, amount);
        }
        saveChallengeHistory(saveChallengeHistory);
    }

    public int cancelCandyAndGetCandyAmount(Long userId, Long challengeId) {
        ChallengeHistory findChallengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId)
                .orElseThrow(() -> new NoSuchElementException("No Such ChallengeHistory"));
        if (findChallengeHistory.isComplete() || findChallengeHistory.getAssignedCandy() == 0) {
            throw new IllegalStateException("Can't Cancel Candy From ChallengeHistory");
        }
        int candyAmount = findChallengeHistory.getAssignedCandy();
        findChallengeHistory.setAssignedCandy(0);
        return candyAmount;
    }

    public int completeChallenge(Long challengeId, Long userId) {
        ChallengeHistory challengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId)
                .orElseThrow(() -> new NoSuchElementException("No Such Challenge"));
        challengeHistory.setComplete(true);
        challengeHistory.setCompleteDate(LocalDateTime.now());
        return challengeHistory.getAssignedCandy();
    }

    public Optional<Challenge> findById(Long challengeId) {
        return challengeRepository.findById(challengeId);
    }

    public ChallengeHistory saveChallengeHistory(ChallengeHistory challengeHistory) {
        return challengeHistoryRepository.save(challengeHistory);
    }

    public Challenge saveChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }
}
