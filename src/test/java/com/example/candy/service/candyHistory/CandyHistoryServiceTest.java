package com.example.candy.service.candyHistory;

import com.example.candy.controller.candyHistory.dto.CandyHistoryResponseDto;
import com.example.candy.domain.candy.CandyHistory;
import com.example.candy.domain.candy.EventType;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.user.User;
import com.example.candy.repository.user.UserRepository;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.user.UserService;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CandyHistoryServiceTest {

    @Autowired
    private CandyHistoryService candyHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired private ChallengeService challengeService;

    private User user;
    private CandyHistory candyHistory_charge;

    @BeforeAll
    void setUp() {
        user = userService.join("a@naver.com", true, "1234", "abcd", "이해석", "01012345678", "19950302");

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
    @Transactional
    void 캔디배정() throws NotFoundException {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        CandyHistory candyHistory = candyHistoryService.assignCandy(user.getId(), "abcd", challenge.getId(), 30);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getParentCandy(), 50);
        assertEquals(candyHistory.getAssignCandy(), 30);
    }

    @Test
    @Transactional
    void 캔디배정_부모비밀번호오류() throws NotFoundException {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        assertThrows(IllegalArgumentException.class, () -> {
            CandyHistory candyHistory = candyHistoryService.assignCandy(user.getId(), "bcds", challenge.getId(), 30);
        });
    }


    @Test
    @Order(5)
    @DisplayName("attian candy")
    @Transactional
    void 캔디_습득() throws NotFoundException {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        candyHistoryService.assignCandy(user.getId(), user.getParentPassword(), challenge.getId(), 30);
        CandyHistory candyHistory = candyHistoryService.attainCandy(user.getId(), challenge.getId());
        assertEquals(candyHistory.getAssignCandy(), 0);
        assertEquals(candyHistory.getStudentCandy(), 30);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getParentCandy(), 50);
    }

    @Test
    @Order(6)
    @DisplayName("with draw candy")
    void 캔디_인출() throws NotFoundException {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        candyHistoryService.assignCandy(user.getId(), user.getParentPassword(), challenge.getId(), 30);
        candyHistoryService.attainCandy(user.getId(), challenge.getId());
        CandyHistory candyHistory = candyHistoryService.withdrawCandy(user.getId(), 20);
        assertEquals(candyHistory.getStudentCandy(), 10);
        assertThrows(IllegalArgumentException.class, () -> {
            candyHistoryService.withdrawCandy(user.getId(), 30);
        });
    }

    @Test
    @Order(7)
    void 캔디_배정취소() throws NotFoundException {
        Challenge challenge = new Challenge();
        challengeService.saveChallenge(challenge);
        candyHistoryService.initCandy(user);
        candyHistoryService.chargeCandy(user.getId(), 80);
        candyHistoryService.assignCandy(user.getId(), user.getParentPassword(), challenge.getId(), 30);
        CandyHistory candyHistory = candyHistoryService.cancelCandy(user.getId(), challenge.getId());
        assertEquals(candyHistory.getStudentCandy(), 0);
        assertEquals(candyHistory.getParentCandy(), 80);
        assertEquals(candyHistory.getTotalCandy(), 80);
        assertEquals(candyHistory.getAssignCandy(), 0);
    }

    @Test
    @Transactional
    @Order(8)
    void 캔디내역_조회_학생_all() throws NotFoundException {
        candyHistoryService.initCandy(user);
        CandyHistory candyHistory = candyHistoryService.chargeCandy(user.getId(), 50);
        CandyHistory candyHistory2 = candyHistoryService.chargeCandy(user.getId(), 30);
        CandyHistory candyHistory3 = candyHistoryService.chargeCandy(user.getId(), 20);
        Challenge challenge = Challenge.builder()
                .title("rap")
                .build();
        Challenge challenge1 = challengeService.saveChallenge(challenge);
        candyHistoryService.assignCandy(user.getId(), user.getParentPassword(), challenge1.getId(), 10);
        candyHistoryService.attainCandy(user.getId(), challenge1.getId());
        candyHistoryService.withdrawCandy(user.getId(), 10);
        List<CandyHistoryResponseDto> candyAll = candyHistoryService.getCandyHistory(user.getId(), "student", "all", 1000L, 5);
        assertEquals(candyAll.size(), 2);
        assertEquals(candyAll.get(0).getEventType(), EventType.WITHDRAW);

        List<CandyHistoryResponseDto> candyAll2 = candyHistoryService.getCandyHistory(user.getId(), "student", "attain", 1000L, 5);
        assertEquals(candyAll2.size(), 1);
        assertEquals(candyAll2.get(0).getEventType(), EventType.ATTAIN);

        assertThrows(IllegalArgumentException.class, () -> candyHistoryService.getCandyHistory(user.getId(), "student", "charge", 1000L, 5));

        List<CandyHistoryResponseDto> candyAll3 = candyHistoryService.getCandyHistory(user.getId(), "parent", "all", 1000L, 5);
        assertEquals(candyAll3.size(), 4);
        assertEquals(candyAll3.get(0).getEventType(), EventType.ASSIGN);
    }
}