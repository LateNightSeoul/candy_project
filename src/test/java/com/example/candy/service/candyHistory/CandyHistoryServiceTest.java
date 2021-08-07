package com.example.candy.service.candyHistory;

import com.example.candy.domain.candy.CandyHistory;
import com.example.candy.domain.candy.EventType;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.user.User;
import com.example.candy.repository.user.UserRepository;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    void 가입시_캔디_초기화() {
        CandyHistory candyHistory = candyHistoryService.initCandy(user);
        assertEquals(candyHistory.getUser().getEmail(), user.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("charge_candy")
    void 캔디_충전() {
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
    void 가장_최근_캔디_조회() {
        CandyHistory findCandy = candyHistoryService.findLatestOne(user.getId());
        assertEquals(findCandy.getTotalCandy(), 130);
    }

    @Test
    @Order(4)
    @DisplayName("with draw candy")
    void 캔디_배정() {
        Challenge challenge = new Challenge();
        challenge.setTitle("test challenge");
        challengeService.saveChallenge(challenge);

        CandyHistory candyHistory = candyHistoryService.assignCandy(user.getId(), challenge.getId(), 30);
        assertEquals(candyHistory.getTotalCandy(), 100);
        assertEquals(candyHistory.getParentCandy(), 100);

    }

    @Test
    @Order(5)
    @DisplayName("with draw candy")
    void 캔디_습득() {

    }

    @Test
    @Order(6)
    @DisplayName("with draw candy")
    void 캔디_인출() {

    }
}