//package com.example.candy.repository.challenge;
//
//import com.example.candy.domain.challenge.ChallengeHistory;
//import com.example.candy.domain.problem.ProblemHistory;
//import com.example.candy.domain.user.User;
//
//import lombok.RequiredArgsConstructor;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.persistence.EntityManager;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//@Repository
//@RequiredArgsConstructor
//public class ProblemHistoryRepositoryImpl implements ProblemHistoryRepository {
//
//	private final EntityManager em;
//
//    @Transactional(readOnly = true)
//    @Override
//    public Optional<ProblemHistory> findByProblem_idAndUser_id(Long problemId) {
//        return em.createQuery("select c from ChallengeHistory c join c.user u" +
//                " join fetch c.challenge ch" +
//                " where u.id = :id", ChallengeHistory.class)
//                .setParameter("id", user.getId())
//                .getResultList();
//    }
//
//}
