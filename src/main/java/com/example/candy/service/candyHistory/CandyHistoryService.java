package com.example.candy.service.candyHistory;

import com.example.candy.controller.candyHistory.dto.CandyHistoryResponseDto;
import com.example.candy.domain.candy.CandyHistory;
import com.example.candy.domain.candy.CandyType;
import com.example.candy.domain.candy.EventType;
import com.example.candy.domain.user.User;
import com.example.candy.repository.candy.CandyHistoryDtoRepository;
import com.example.candy.repository.candy.CandyHistoryRepository;
import com.example.candy.repository.challenge.ChallengeHistoryRepository;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandyHistoryService {

    @Autowired private CandyHistoryRepository candyHistoryRepository;
    @Autowired private ChallengeHistoryRepository challengeHistoryRepository;
    @Autowired private UserService userService;
    @Autowired private ChallengeService challengeService;
    @Autowired private CandyHistoryDtoRepository candyHistoryDtoRepository;

    @Transactional
    public CandyHistory initCandy(User user) {
        CandyHistory candyHistory = CandyHistory.builder()
                .totalCandy(0)
                .eventType(EventType.INIT)
                .parentCandy(0)
                .studentCandy(0)
                .assignCandy(0)
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
        if (amount <= 0) {
            throw new IllegalArgumentException("Not valid amount");
        }
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
        if (amount <= 0) {
            throw new IllegalArgumentException("Not valid amount");
        }
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("no such userId"));

        CandyHistory latestCandy = findLatestOne(userId);
        if (latestCandy.getStudentCandy() < amount) {
            throw new IllegalArgumentException("Not Enough Candy To Withdraw");
        }
        CandyHistory candy = CandyHistory.builder()
                .user(user)
                .createDate(LocalDateTime.now())
                .amount(amount)
                .studentCandy(latestCandy.getStudentCandy() - amount)
                .parentCandy(latestCandy.getParentCandy())
                .totalCandy(latestCandy.getTotalCandy() - amount)
                .eventType(EventType.WITHDRAW)
                .build();
        return save(candy);
    }

    @Transactional
    public CandyHistory assignCandy(Long userId, Long challengeId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Not valid amount");
        }
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such UserId"));
        CandyHistory latestCandy = findLatestOne(userId);
        if (latestCandy.getParentCandy() < amount) {
            throw new IllegalArgumentException("Not Enough Candy To assign");
        }

        challengeService.assignCandyInChallengeHistory(challengeId, amount, user);

        CandyHistory candyHistory = CandyHistory.builder()
                .eventType(EventType.ASSIGN)
                .totalCandy(latestCandy.getTotalCandy())
                .parentCandy(latestCandy.getParentCandy() - amount)
                .studentCandy(latestCandy.getStudentCandy())
                .assignCandy(amount)
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
        CandyHistory latestCandy = findLatestOne(userId);

        if (latestCandy.getAssignCandy() < candyAmount) {
            throw new IllegalArgumentException("assignCandy is less than cancelCandyAmount");
        }
        CandyHistory candyHistory = CandyHistory.builder()
                .user(user)
                .createDate(LocalDateTime.now())
                .amount(candyAmount)
                .assignCandy(latestCandy.getAssignCandy() - candyAmount)
                .studentCandy(latestCandy.getStudentCandy())
                .parentCandy(latestCandy.getParentCandy() + candyAmount)
                .totalCandy(latestCandy.getTotalCandy())
                .eventType(EventType.CANCEL)
                .build();
        return save(candyHistory);
    }

    @Transactional
    public CandyHistory attainCandy(Long userId, Long challengeId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such UserId"));
        int attainCandyAmount = challengeService.completeChallenge(challengeId, userId);
        CandyHistory latestCandy = findLatestOne(userId);

        if (latestCandy.getAssignCandy() < attainCandyAmount) {
            throw new IllegalArgumentException("assignCandy is less than attainCandyAmount");
        }
        CandyHistory candyHistory = CandyHistory.builder()
                .eventType(EventType.ATTAIN)
                .totalCandy(latestCandy.getTotalCandy())
                .parentCandy(latestCandy.getParentCandy())
                .studentCandy(latestCandy.getStudentCandy() + attainCandyAmount)
                .assignCandy(latestCandy.getAssignCandy() - attainCandyAmount)
                .amount(attainCandyAmount)
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
        return save(candyHistory);
    }

    public List<CandyHistoryResponseDto> getCandyHistory(Long userId, String identity, String category, Long lastCandyHistoryId, int size) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such UserId"));

        if (identity == "student") {
            if (category != "all" && category != "attain" && category != "withdraw") {
                throw new IllegalArgumentException("not a valid category");
            }
        } else if (identity == "parent") {
            if (category != "all" && category != "charge" && category != "assign" && category != "cancel") {
                throw new IllegalArgumentException("not a valid category");
            }
        } else {
            throw new IllegalArgumentException("not a valid identity");
        }

        return candyHistoryDtoRepository.findCandyHistory(userId, identity, category, lastCandyHistoryId, size);
    }

    public int findCandyAmount(Long userId, String candyType) {
        CandyHistory latestCandy = findLatestOne(userId);
        if (candyType.equalsIgnoreCase(CandyType.STUDENT.toString())) {
            return latestCandy.getStudentCandy();
        } else if (candyType.equalsIgnoreCase(CandyType.PARENT.toString())) {
            return latestCandy.getParentCandy();
        } else {
            throw new IllegalArgumentException("Not a valid CandyType");
        }
    }

    public CandyHistory findLatestOne(Long userId) { return candyHistoryRepository.findTopByUser_IdOrderByCreateDateDesc(userId); }

    public CandyHistory save(CandyHistory candyHistory) {
        return candyHistoryRepository.save(candyHistory);
    }
}
