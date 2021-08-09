package com.example.candy.repository.challenge;

import com.example.candy.controller.challenge.ChallengeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChallengeDtoRepository {

    private final EntityManager em;



    @Transactional
    public List<ChallengeDto> findChallenges(Long userId) {
        return em.createQuery(
                "select new " +
                        "com.example.candy.controller.challenge.ChallengeDto(c.id,c.category,c.title,c.subTitle,coalesce(cl.user.id,false) ,c.totalScore,c.requiredScore)" +
                        " from Challenge c" +
                        " left join ChallengeLike cl on c.id = cl.challenge.id" +
                        " and cl.user.id = : userId", ChallengeDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
