package com.example.candy.repository.candy;

import com.example.candy.controller.candyHistory.dto.CandyHistoryResponseDto;
import com.example.candy.controller.challenge.dto.ChallengeDto;
import com.example.candy.domain.candy.EventType;
import com.example.candy.domain.candy.QCandyHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<CandyHistoryResponseDto> findCandyHistory(Long userId, String identity, String category, Long lastCandyHistoryId, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCandyHistory candyHistory = new QCandyHistory("ch");

        BooleanBuilder builder= new BooleanBuilder();
        builder.and(candyHistory.user.id.eq(userId));
        builder.and(candyHistory.id.lt(lastCandyHistoryId));

        if (identity.equals("student")) {
            if (category.equals("all")) {
                builder.andAnyOf(candyHistory.eventType.eq(EventType.valueOf("ATTAIN")),
                            candyHistory.eventType.eq(EventType.valueOf("WITHDRAW")));
            } else if (category.equals("attain")) {
                builder.and(candyHistory.eventType.eq(EventType.valueOf("ATTAIN")));
            } else if (category.equals("withdraw")) {
                builder.and(candyHistory.eventType.eq(EventType.valueOf("WITHDRAW")));
            }
        } else if (identity.equals("parent")) {
            if (category.equals("all")) {
                builder.andAnyOf(candyHistory.eventType.eq(EventType.valueOf("CHARGE")),
                            candyHistory.eventType.eq(EventType.valueOf("ASSIGN")),
                            candyHistory.eventType.eq(EventType.valueOf("CANCEL")));
            } else if (category.equals("charge")) {
                builder.and(candyHistory.eventType.eq(EventType.valueOf("CHARGE")));
            } else if (category.equals("assign")) {
                builder.and(candyHistory.eventType.eq(EventType.valueOf("ASSIGN")));
            } else if (category.equals("cancel")) {
                builder.and(candyHistory.eventType.eq(EventType.valueOf("CANCEL")));
            }
        }

        return queryFactory
                .select(Projections.constructor(CandyHistoryResponseDto.class,
                        candyHistory.id, candyHistory.eventType, candyHistory.createDate, candyHistory.amount))
                .from(candyHistory)
                .where(builder)
                .orderBy(candyHistory.id.desc())
                .limit(size)
                .fetch();
    }
}
