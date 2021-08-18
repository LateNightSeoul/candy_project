package com.example.candy.service.challenge;

import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.user.User;
import com.example.candy.error.NotFoundException;
import com.example.candy.repository.challenge.ChallengeLikeRepository;
import com.example.candy.repository.challenge.ChallengeRepository;
import com.example.candy.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeLikeService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    ChallengeLikeRepository challengeLikeRepository;

    @Transactional(readOnly = true)
    public Optional<ChallengeLike> findChallengeLike(Long userId, Long challengeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException("Challenge Not Found"));

        return challengeLikeRepository.findByUserAndChallenge(user,challenge);
    }

    @Transactional(readOnly = true)
    public List<ChallengeLike> findAll(Long userId, Long lastChallengeId, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        PageRequest pageRequest = PageRequest.of(0, size); // 페이지네이션을 위한 PageRequest, 페이지는 0으로 고정한다.
        Page<ChallengeLike> challengeLikePage = challengeLikeRepository.findByUserAndChallenge_idLessThanOrderByChallenge_idDesc(user, lastChallengeId, pageRequest);
        List<ChallengeLike> challengeLikeList = challengeLikePage.getContent();
        return challengeLikeList;
    }

    @Transactional
    public ChallengeLike like(Long userId, Long challengeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException("Challenge Not Found"));

        ChallengeLike challengeLike = new ChallengeLike();
        challengeLike.setChallenge(challenge);
        challengeLike.setUser(user);
        challengeLike.setCreateDate(LocalDateTime.now());
        return challengeLikeRepository.save(challengeLike);
    }

    @Transactional
    public void delete(Long userId, Long challengeId) {
        challengeLikeRepository.deleteByUser_idAndChallenge_id(userId, challengeId);
    }
}
