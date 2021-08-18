package com.example.candy.service.candyHistory;

import com.example.candy.controller.candyHistory.dto.CandyHistoryResponseDto;
import com.example.candy.domain.candy.CandyHistory;
import com.example.candy.domain.candy.EventType;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.user.User;
import com.example.candy.repository.user.UserRepository;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CandyHistoryServiceTest {

    @Autowired
    private CandyHistoryService candyHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired private ChallengeService challengeService;

    private User user;
    private CandyHistory candyHistory_charge;

    @BeforeAll
    void setUp() {
        user = User.builder()
                .email("a@naver.com")
                .password("1234")
                .parentPassword("abcd")
                .name("이해석")
                .birth("19950302")
                .phone("01012345678")
                .build();
        userRepository.save(user);
    }

    @Test
    @Order(1)
    @DisplayName("init_candy")
    @Transactional
    void 가입시_캔디_초기화() {
        CandyHistory candyHistory = candyHistoryService.initCandy(user);
        assertEquals(candyHistory.getUser().getEmail(), user.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("charge_candy")
    @Transactional
    void 캔디_충전() {
        candyHistoryService.initCandy(user);
        CandyHistory candyHistory = candyHistoryService.chargeCandy(user.getId(), 80);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getStudentCandy(), 0);
        CandyHistory findCandy = candyHistoryService.findLatestOne(user.getId());
        assertEquals(findCandy.getTotalCandy(), 80);
        assertEquals(findCandy.getParentCandy(), 80);

        CandyHistory candyHistory2 = candyHistoryService.chargeCandy(user.getId(), 50);
        assertEquals(candyHistory2.getTotalCandy(), 130);
        assertEquals(candyHistory2.getStudentCandy(), 0);
        CandyHistory findCandy2 = candyHistoryService.findLatestOne(user.getId());
        assertEquals(findCandy2.getTotalCandy(), 130);
        assertEquals(findCandy2.getParentCandy(), 130);
    }

    @Test
    @Order(3)
    @DisplayName("find_candy")
    @Transactional
    void 가장_최근_캔디_조회() {
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        CandyHistory findCandy = candyHistoryService.findLatestOne(user.getId());
        assertEquals(findCandy.getTotalCandy(), 80);
    }

    @Test
    @Order(4)
    @DisplayName("with draw candy")
    @Transactional
    void 캔디_배정() {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        CandyHistory candyHistory = candyHistoryService.assignCandy(user.getId(), challenge.getId(), 30);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getParentCandy(), 50);
        assertEquals(candyHistory.getAssignCandy(), 30);
    }

    @Test
    @Order(5)
    @DisplayName("attian candy")
    @Transactional
    void 캔디_습득() {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        candyHistoryService.assignCandy(user.getId(), challenge.getId(), 30);
        CandyHistory candyHistory = candyHistoryService.attainCandy(user.getId(), challenge.getId());
        assertEquals(candyHistory.getAssignCandy(), 0);
        assertEquals(candyHistory.getStudentCandy(), 30);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getParentCandy(), 50);
    }

    @Test
    @Order(6)
    @DisplayName("with draw candy")
    void 캔디_인출() {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        candyHistoryService.assignCandy(user.getId(), challenge.getId(), 30);
        candyHistoryService.attainCandy(user.getId(), challenge.getId());
        CandyHistory candyHistory = candyHistoryService.withdrawCandy(user.getId(), 20);
        assertEquals(candyHistory.getStudentCandy(), 10);
        assertThrows(IllegalArgumentException.class, () -> {
            candyHistoryService.withdrawCandy(user.getId(), 30);
        });
    }

    @Test
    @Order(7)
    void 캔디_배정취소() {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        candyHistoryService.assignCandy(user.getId(), challenge.getId(), 30);
        CandyHistory candyHistory = candyHistoryService.cancelCandy(user.getId(), challenge.getId());
        assertEquals(candyHistory.getStudentCandy(), 0);
        assertEquals(candyHistory.getParentCandy(), 80);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getAssignCandy(), 0);
    }

    @Test
    @Transactional
    @Order(8)
    void 캔디내역_조회_학생_all() {
        candyHistoryService.initCandy(user);
        CandyHistory candyHistory = candyHistoryService.chargeCandy(user.getId(), 50);
        CandyHistory candyHistory2 = candyHistoryService.chargeCandy(user.getId(), 30);
        CandyHistory candyHistory3 = candyHistoryService.chargeCandy(user.getId(), 20);
        Challenge challenge = Challenge.builder()
                .title("rap")
                .build();
        Challenge challenge1 = challengeService.saveChallenge(challenge);
        candyHistoryService.assignCandy(user.getId(), challenge1.getId(), 10);
        candyHistoryService.attainCandy(user.getId(), challenge1.getId());
        List<CandyHistoryResponseDto> candyAll = candyHistoryService.getStudentCandyAll(user.getId(), "student", "all", 1000L, 5);
        System.out.println("******************");
        for (CandyHistoryResponseDto candy : candyAll) {
            System.out.println("candy Amount: " + candy.getAmount() + "  candy Amount: " + candy.getCreateDate()
            + "  candy Event" + candy.getEventType());

        }
        System.out.println("******************");

        List<CandyHistoryResponseDto> candyAll2 = candyHistoryService.getStudentCandyAll(user.getId(), "student", "attain", 1000L, 5);
        System.out.println("******************");
        for (CandyHistoryResponseDto candy : candyAll2) {
            System.out.println("candy Amount: " + candy.getAmount() + "  candy Amount: " + candy.getCreateDate()
                    + "  candy Event" + candy.getEventType());

        }
        System.out.println("******************");

        assertThrows(InvalidDataAccessApiUsageException.class, () -> candyHistoryService.getStudentCandyAll(user.getId(), "student", "charge", 1000L, 5));
    }
}