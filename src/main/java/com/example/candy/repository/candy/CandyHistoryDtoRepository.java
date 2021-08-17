package com.example.candy.repository.candy;

import com.example.candy.controller.candyHistory.dto.CandyHistoryResponseDto;
import com.example.candy.controller.challenge.dto.ChallengeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CandyHistoryDtoRepository {
    private final EntityManager em;

    @Transactional
    public List<CandyHistoryResponseDto> findStudentCandyAll(Long userId, Long lastCandyHistoryId, int size) {
        return em.createQuery(
                "select new " +
                        "com.example.candy.controller.candyHistory.dto.CandyHistoryResponseDto(" +
                        "ch.eventType, ch.createDate, ch.amount)" +
                        " from CandyHistory ch" +
                        " where ch.id < :lastCandyHistoryId" +
                        " and ch.user.id = :userId" +
                        " order by ch.id desc" , CandyHistoryResponseDto.class)
                .setParameter("userId", userId)
                .setParameter("lastCandyHistoryId", lastCandyHistoryId)
                .setMaxResults(size)
                .getResultList();
    }
}
