package com.example.candy.service.challenge;

import com.example.candy.controller.challenge.dto.ChallengeDetailResponseDto;
import com.example.candy.controller.challenge.dto.ProblemMarkingRSDto;
import com.example.candy.controller.challenge.dto.ProblemSolvedRequestDto;
import com.example.candy.controller.challenge.dto.ProblemSolvedRequestDtoList;
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
import java.util.*;

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
    public int grading(Long userId, ProblemSolvedRequestDtoList problemSolvedRequestDtoList) {
        ChallengeHistory challengeHistory = findChallengeHistory(problemSolvedRequestDtoList.getChallengeId(), userId);
        List<ProblemSolvedRequestDto> problemSolvedRequestDto = problemSolvedRequestDtoList.getProblemSolvedRequestDto();
        List<Problem> problemList = findProblemByChallengeId(problemSolvedRequestDtoList.getChallengeId());
        Map<Long, Problem> problemMap = Problem.listToMap(problemList);
        int totalScore = 0;

        for (ProblemSolvedRequestDto problemSolvedDto : problemSolvedRequestDto) {
            if (!problemMap.containsKey(problemSolvedDto.getProblemId())) {
                throw new IllegalStateException("Not Exist problemId");
            }
            if (problemSolvedDto.getProblemScore() == null) {
                throw new IllegalStateException("Need Problem Score");
            }
            ProblemHistory problemHistory = ProblemHistory.builder()
                    .challengeHistory(challengeHistory)
                    .problem(problemMap.get(problemSolvedDto.getProblemId()))
                    .isSuccess(problemSolvedDto.getProblemScore() > 0)
                    .problemScore(problemSolvedDto.getProblemScore())
                    .build();
            totalScore += problemSolvedDto.getProblemScore();
            solvedProblem(problemHistory);
        }
        int highestScore = challengeHistory.saveScore(totalScore);
        saveChallengeHistory(challengeHistory);
        return totalScore;
    }

    @Transactional
    public void solvedProblem(ProblemHistory problemHistory) {
    	Optional<ProblemHistory> OproblemHistory = problemHistoryRepository.findByProblem_id(problemHistory.getProblem().getId());
    	
    	OproblemHistory.ifPresentOrElse(CproblemHistory -> {
    		CproblemHistory.setId(OproblemHistory.get().getId());
    		CproblemHistory.setChallengeHistory(problemHistory.getChallengeHistory());
    		CproblemHistory.setSuccess(problemHistory.isSuccess());
    		CproblemHistory.setProblem(problemHistory.getProblem());
            CproblemHistory.setProblemScore(problemHistory.getProblemScore());
    		
    		problemHistoryRepository.save(CproblemHistory);
    	}, () -> {problemHistoryRepository.save(problemHistory);});
    }
    
//     @Transactional
//     public ProblemMarkingRSDto markedProblem(ProblemHistory problemHistory) {
// //    	Optional<ProblemHistory> OproblemHistory = problemHistoryRepository.findByProblem_id(problemHistory.getProblem().getId());
//     	String answerMark;
    	
//     	Problem problem = problemRepository.findById(problemHistory.getProblem().getId())
//     			.orElseThrow(() -> new NotFoundException("Problem Not Found"));
    	
//     	if(problem.isMultiple() == true) {
//     		answerMark = (problem.getMultipleAnswer() == problemHistory.getMultipleAnswer()) ? "O" : "X";
    		
    		
//     	}else {
//     		answerMark = (problem.getAnswer() == problemHistory.getAnswer()) ? "O" : "X";
//     	}
    	
//     	if(Objects.equals(answerMark, "O")) {
// 			problemHistory.setSuccess(true);
// 		}
    	
//     	return new ProblemMarkingRSDto(answerMark); 
//     }


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
        challengeHistory.setProgress(true);
        challengeHistory.setScore(0);
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
        findChallengeHistory.setProgress(false);
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
        challengeHistory.setProgress(false);
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
        Page<ChallengeHistory> page = challengeHistoryRepository.findByUserAndCompleteAndProgressAndChallenge_idLessThanOrderByChallenge_idDesc(user, true, false,lastChallengeId, pageRequest);
        List<ChallengeHistory> challengeHistoryList = page.getContent();
        return challengeHistoryList;
    }

    //완료되지 않은 challengeHistory 찾기
    public List<ChallengeHistory> notCompletedChallengeList(Long userId, boolean complete,Long lastChallengeId, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        PageRequest pageRequest = PageRequest.of(0, size); // 페이지네이션을 위한 PageRequest, 페이지는 0으로 고정한다.
        Page<ChallengeHistory> page = challengeHistoryRepository.findByUserAndCompleteAndProgressAndChallenge_idLessThanOrderByChallenge_idDesc(user, false, true,lastChallengeId, pageRequest);
        List<ChallengeHistory> challengeHistoryList = page.getContent();
        return challengeHistoryList;
    }

    public Problem findProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() -> new NotFoundException("Problem Not Found"));

        return problem;
    }

    public List<Problem> findProblemByChallengeId(Long challengeId) {
        return problemRepository.findByChallenge_Id(challengeId);
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
    
    public Challenge findChallenge(Long challengeId) {
    	Challenge challenge = findChallengeById(challengeId)
    			.orElseThrow(() -> new NotFoundException("Challenge Not Found"));
    	
    	return challenge;
    }

    public int findScore(Long challengeId, Long userId) {
        ChallengeHistory challengeHistory = challengeHistoryRepository.findByChallenge_idAndUser_id(challengeId, userId)
                .orElseThrow(() -> new NotFoundException("Challenge History Not Found"));
        return challengeHistory.getScore();
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
