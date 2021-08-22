package com.example.candy.service.challenge;

import com.example.candy.controller.challenge.dto.ChallengeDetailResponseDto;
import com.example.candy.controller.challenge.dto.ProblemMarkingRSDto;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.problem.ProblemHistory;
import com.example.candy.domain.user.User;
import com.example.candy.enums.Category;
import com.example.candy.error.NotFoundException;
import com.example.candy.repository.challenge.ChallengeDtoRepository;
import com.example.candy.repository.challenge.ChallengeHistoryRepository;
import com.example.candy.repository.challenge.ChallengeLikeRepository;
import com.example.candy.repository.challenge.ChallengeRepository;
import com.example.candy.repository.challenge.ProblemHistoryRepository;
import com.example.candy.repository.challenge.ProblemRepository;
import com.example.candy.repository.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeLikeRepository challengeLikeRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final ChallengeDtoRepository challengeDtoRepository;
    private final ProblemRepository problemRepository;
    private final ProblemHistoryRepository problemHistoryRepository;

    public ChallengeService(UserRepository userRepository, ChallengeRepository challengeRepository, ChallengeLikeRepository challengeLikeRepository,ChallengeHistoryRepository challengeHistoryRepository, ChallengeDtoRepository challengeDtoRepository, ProblemRepository problemRepository, ProblemHistoryRepository problemHistoryRepository) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.challengeLikeRepository = challengeLikeRepository;
        this.challengeHistoryRepository = challengeHistoryRepository;
        this.challengeDtoRepository = challengeDtoRepository;
        this.problemRepository = problemRepository;
        this.problemHistoryRepository = problemHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Challenge> findAllChallenges() {
        return challengeRepository.findAll();
    }

    // 필요한 것. user id
    @Transactional(readOnly = true)
    public List<ChallengeHistory> findChallengeHistories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        List<ChallengeHistory> challengeHistories = challengeHistoryRepository.findAllByUser(user);
        return challengeHistories;

    }

    @Transactional(readOnly = true)
    public List<ChallengeHistory> categoryChallengeHistories(Long userId, Category category) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        List<ChallengeHistory> challengeHistory = challengeHistoryRepository.findAllByUserAndCategory(user, category);
        return challengeHistory;
    }

    @Transactional
    public Challenge registerChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @Transactional
    public void solvedProblem(ProblemHistory problemHistory) {
    	Optional<ProblemHistory> OproblemHistory = problemHistoryRepository.findByProblem_id(problemHistory.getProblem().getId());
    	
    	OproblemHistory.ifPresentOrElse(CproblemHistory -> {
    		CproblemHistory.setId(OproblemHistory.get().getId());
    		CproblemHistory.setAnswer(problemHistory.getAnswer());
    		CproblemHistory.setChallengeHistory(problemHistory.getChallengeHistory());
    		CproblemHistory.setMultiple(problemHistory.isMultiple());
    		CproblemHistory.setSuccess(problemHistory.isSuccess());
    		CproblemHistory.setProblem(problemHistory.getProblem());
    		CproblemHistory.setMultipleAnswer(problemHistory.getMultipleAnswer());
    		
    		problemHistoryRepository.save(CproblemHistory);
    	}, () -> {problemHistoryRepository.save(problemHistory);});
    }
    
    @Transactional
    public ProblemMarkingRSDto markedProblem(ProblemHistory problemHistory) {
//    	Optional<ProblemHistory> OproblemHistory = problemHistoryRepository.findByProblem_id(problemHistory.getProblem().getId());
    	String answerMark;
    	
    	Problem problem = problemRepository.findById(problemHistory.getProblem().getId())
    			.orElseThrow(() -> new NotFoundException("Problem Not Found"));
    	
    	if(problem.isMultiple() == true) {
    		answerMark = (problem.getMultipleAnswer() == problemHistory.getMultipleAnswer()) ? "O" : "X";
    	}else {
    		answerMark = (problem.getAnswer() == problemHistory.getAnswer()) ? "O" : "X";
    	}
    	
    	return new ProblemMarkingRSDto(answerMark); 
    }


    public ChallengeHistory assignCandyInChallengeHistory(Long challengeId, int amount, User user) {
        Optional<ChallengeHistory> findChallengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, user.getId());
        ChallengeHistory challengeHistory;
        if (findChallengeHistory.isPresent()) {
            if (findChallengeHistory.get().isComplete() || findChallengeHistory.get().getAssignedCandy() != 0) {
                throw new IllegalStateException("ChallengeHistory Already Exists");
            }
            findChallengeHistory.get().setAssignedCandy(amount);
            challengeHistory = findChallengeHistory.get();
        } else {
            Challenge findChallenge = findChallengeById(challengeId)
                    .orElseThrow(() -> new NoSuchElementException("No Such Challenge"));
            challengeHistory = new ChallengeHistory(user, findChallenge, amount);
        }
        return saveChallengeHistory(challengeHistory);
    }

    public int cancelCandyAndGetCandyAmount(Long userId, Long challengeId) {
        ChallengeHistory findChallengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId)
                .orElseThrow(() -> new NoSuchElementException("No Such ChallengeHistory"));
        if (findChallengeHistory.isComplete() || findChallengeHistory.getAssignedCandy() == 0) {
            throw new IllegalStateException("Can't Cancel Candy From ChallengeHistory");
        }
        int candyAmount = findChallengeHistory.getAssignedCandy();
        findChallengeHistory.setAssignedCandy(0);
        saveChallengeHistory(findChallengeHistory);
        return candyAmount;
    }

    public int completeChallenge(Long challengeId, Long userId) {
        ChallengeHistory challengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId)
                .orElseThrow(() -> new NoSuchElementException("No Such ChallengeHistory"));
        if (challengeHistory.getAssignedCandy() <= 0) {
            throw new IllegalStateException("Assigned Candy is below 0");
        }
        int assignedCandy = challengeHistory.getAssignedCandy();
        challengeHistory.setComplete(true);
        challengeHistory.setCompleteDate(LocalDateTime.now());
        challengeHistory.setAssignedCandy(0);
        challengeHistory.setModifiedDate(LocalDateTime.now());
        saveChallengeHistory(challengeHistory);
        return assignedCandy;
    }

    //완료된 challengeHistory 찾기
    public List<ChallengeHistory> completedChallengeList(Long userId, boolean complete, Long lastChallengeId, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        PageRequest pageRequest = PageRequest.of(0, size); // 페이지네이션을 위한 PageRequest, 페이지는 0으로 고정한다.
        Page<ChallengeHistory> page = challengeHistoryRepository.findByUserAndCompleteAndChallenge_idLessThanOrderByChallenge_idDesc(user, true, lastChallengeId, pageRequest);
        List<ChallengeHistory> challengeHistoryList = page.getContent();
        return challengeHistoryList;
    }

    //완료되지 않은 challengeHistory 찾기
    public List<ChallengeHistory> notCompletedChallengeList(Long userId, boolean complete,Long lastChallengeId, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        PageRequest pageRequest = PageRequest.of(0, size); // 페이지네이션을 위한 PageRequest, 페이지는 0으로 고정한다.
        Page<ChallengeHistory> page = challengeHistoryRepository.findByUserAndCompleteAndChallenge_idLessThanOrderByChallenge_idDesc(user, false, lastChallengeId, pageRequest);
        List<ChallengeHistory> challengeHistoryList = page.getContent();
        return challengeHistoryList;
    }

    public Problem findProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() -> new NotFoundException("Problem Not Found"));

        return problem;
    }
    
    public ProblemHistory findProblemHistory(Long challengeHistoryId, Long problemId) {
    	ProblemHistory problemHistory = problemHistoryRepository.findByChallengeHistory_idAndProblem_id(challengeHistoryId, problemId)
    		.orElseThrow(() -> new NotFoundException("Problem History Not Found"));
    	
    	return problemHistory;
    }
    
    public ChallengeHistory findChallengeHistory(Long challengeId, Long userId) {
    	ChallengeHistory challengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId)
    		.orElseThrow(() -> new NotFoundException("Challenge History Not Found"));
    	
    	return challengeHistory;
    }

    public ChallengeDetailResponseDto findChallengeDetail(Long userId, Long challengeId) {
        return challengeDtoRepository.findChallengeDetail(userId, challengeId)
                .orElseThrow(() -> new NotFoundException("No Such Challenge"));
    }

    public Optional<Challenge> findChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId);
    }

    public Optional<ChallengeHistory> findChallengeHistoryById(Long challengeId, Long userId) {return challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId);}

    public ChallengeHistory saveChallengeHistory(ChallengeHistory challengeHistory) {
        return challengeHistoryRepository.save(challengeHistory);
    }

    public Challenge saveChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }
}
