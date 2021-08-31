package com.example.candy.repository.challenge;

import com.example.candy.controller.challenge.dto.ChallengeDetailResponseDto;
import com.example.candy.controller.challenge.dto.ChallengeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChallengeDtoRepository {

    private final EntityManager em;

    @Transactional
    public List<ChallengeDto> findChallenges(Long userId, Long lastChallengeId, int size) {
        return em.createQuery(
                "select new " +
                        "com.example.candy.controller.challenge.dto.ChallengeDto(c.id,c.category,c.title,c.subTitle," +
                        "cl.id," +
                        "c.totalScore,c.requiredScore)" +
                        " from Challenge c" +
                        " left join ChallengeHistory ch on c.id = ch.challenge.id" +
                        " and ch.user.id = :userId" +
                        " left join ChallengeLike cl on c.id = cl.challenge.id" +
                        " and cl.user.id = :userId" +
                        " where c.id < :lastChallengeId" +
                        " and ch.challenge.id is null " +
                        " order by c.id desc" , ChallengeDto.class)
                .setParameter("userId", userId)
                .setParameter("lastChallengeId", lastChallengeId)
                .setMaxResults(size)
                .getResultList();
    }

    @Transactional
    public Optional<ChallengeDetailResponseDto> findChallengeDetail(Long userId, Long challengeId) {
        ChallengeDetailResponseDto challengeDetailResponseDto = em.createQuery(
                "select new " +
                        "com.example.candy.controller.challenge.dto.ChallengeDetailResponseDto(" +
                        "c.id, c.title, c.subTitle, ch.complete, ch.assignedCandy, cl.id, c.category, c.description," +
                        "c.totalScore, c.requiredScore, c.level, c.problemCount" +
                        ")" +
                        "from Challenge c" +
                        " left join ChallengeLike cl on c.id = cl.challenge.id" +
                        " and cl.user.id = :userId" +
                        " left join ChallengeHistory ch on c.id = ch.challenge.id" +
                        " and ch.user.id = :userId" +
                        " where c.id = :challengeId", ChallengeDetailResponseDto.class)
                .setParameter("userId", userId)
                .setParameter("userId", userId)
                .setParameter("challengeId", challengeId)
                .getSingleResult();
        return Optional.ofNullable(challengeDetailResponseDto);
    }
}
