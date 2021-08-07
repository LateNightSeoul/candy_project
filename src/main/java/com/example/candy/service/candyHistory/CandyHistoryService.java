package com.example.candy.service.candyHistory;

import com.example.candy.domain.candy.CandyHistory;
import com.example.candy.domain.candy.EventType;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.user.User;
import com.example.candy.repository.candy.CandyHistoryRepository;
import com.example.candy.repository.challenge.ChallengeHistoryRepository;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class CandyHistoryService {

    @Autowired
    private CandyHistoryRepository candyHistoryRepository;
    @Autowired
    private ChallengeHistoryRepository challengeHistoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChallengeService challengeService;

    @Transactional
    public CandyHistory initCandy(User user) {
        CandyHistory candyHistory = CandyHistory.builder()
                .totalCandy(0)
                .eventType(EventType.INIT)
                .parentCandy(0)
                .studentCandy(0)
                .amount(0)
                .user(user)
                .createDate(LocalDateTime.now())
                .build();
        CandyHistory savedCandyHistory = save(candyHistory);
        savedCandyHistory.addUser(user);
        return savedCandyHistory;
    }

    @Transactional
    public CandyHistory chargeCandy(Long userId, int amount) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("no such userId"));

        CandyHistory latestCandy = findLatestOne(userId);
        CandyHistory candyHistory = CandyHistory.builder()
                .totalCandy(latestCandy.getTotalCandy() + amount)
                .eventType(EventType.CHARGE)
                .parentCandy(latestCandy.getParentCandy() + amount)
                .studentCandy(latestCandy.getStudentCandy())
                .amount(amount)
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
        return save(candyHistory);
    }

    @Transactional
    public CandyHistory withdrawCandy(Long userId, int amount) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("no such userId"));

        CandyHistory latestOne = findLatestOne(userId);
        if (latestOne.getStudentCandy() < amount) {
            throw new IllegalArgumentException("Not Enough Candy To Withdraw");
        }
        CandyHistory candy = CandyHistory.builder()
                .user(user)
                .createDate(LocalDateTime.now())
                .amount(amount)
                .studentCandy(latestOne.getStudentCandy() - amount)
                .parentCandy(latestOne.getParentCandy())
                .totalCandy(latestOne.getTotalCandy() - amount)
                .eventType(EventType.WITHDRAW)
                .build();
        return save(candy);
    }

    @Transactional
    public CandyHistory assignCandy(Long userId, Long challengeId, int amount) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such UserId"));
        CandyHistory latestOne = findLatestOne(userId);
        if (latestOne.getParentCandy() < amount) {
            throw new IllegalArgumentException("Not Enough Candy To assign");
        }

        challengeService.assignCandyInChallengeHistory(challengeId, amount, user);

        CandyHistory candyHistory = CandyHistory.builder()
                .eventType(EventType.ASSIGN)
                .totalCandy(latestOne.getTotalCandy() - amount)
                .parentCandy(latestOne.getParentCandy() - amount)
                .studentCandy(latestOne.getStudentCandy())
                .amount(amount)
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
        return save(candyHistory);
    }

    @Transactional
    public CandyHistory cancelCandy(Long userId, Long challengeId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such UserId"));
        int candyAmount = challengeService.cancelCandyAndGetCandyAmount(userId, challengeId);
        CandyHistory latestOne = findLatestOne(userId);
        CandyHistory candyHistory = CandyHistory.builder()
                .user(user)
                .createDate(LocalDateTime.now())
                .amount(candyAmount)
                .studentCandy(latestOne.getStudentCandy())
                .parentCandy(latestOne.getParentCandy() + candyAmount)
                .totalCandy(latestOne.getTotalCandy() + candyAmount)
                .eventType(EventType.CANCEL)
                .build();
        return save(candyHistory);
    }

    @Transactional
    public CandyHistory attainCandy(Long userId, Long challengeId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such UserId"));
        int attainCandyAmount = challengeService.completeChallenge(challengeId, userId);
        CandyHistory latestOne = findLatestOne(userId);
        CandyHistory candyHistory = CandyHistory.builder()
                .eventType(EventType.ATTAIN)
                .totalCandy(latestOne.getTotalCandy())
                .parentCandy(latestOne.getParentCandy())
                .studentCandy(latestOne.getStudentCandy() + attainCandyAmount)
                .amount(attainCandyAmount)
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
        return save(candyHistory);
    }

    public int candyStudent(Long userId) {
        CandyHistory latestOne = findLatestOne(userId);
        return latestOne.getStudentCandy();
    }

    public int candyParent(Long userId) {
        CandyHistory latestOne = findLatestOne(userId);
        return latestOne.getParentCandy();
    }

    public CandyHistory findLatestOne(Long userId) { return candyHistoryRepository.findTopByUser_IdOrderByCreateDateDesc(userId); }

    public CandyHistory save(CandyHistory candyHistory) {
        return candyHistoryRepository.save(candyHistory);
    }
}
