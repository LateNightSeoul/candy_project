package com.example.candy.controller.challenge;

import com.example.candy.controller.ApiResult;
import com.example.candy.controller.challenge.dto.*;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.repository.challenge.ChallengeDtoRepository;
import com.example.candy.security.JwtAuthentication;
import com.example.candy.service.challenge.ChallengeLikeService;
import com.example.candy.service.challenge.ChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/challenge")
@Api(tags = {"챌린지"})
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ChallengeLikeService challengeLikeService;
    @Autowired
    private ChallengeDtoRepository challengeDtoRepository;

    @PostMapping("/register")
    @ApiOperation(value = "챌린지 등록")
    public ApiResult<ChallengeRegisterResponseDto> register(@RequestBody @ApiParam ChallengeRegisterRequestDto challengeRegisterRequestDto) {

        Challenge challenge = Challenge.builder()
                .title(challengeRegisterRequestDto.getTitle())
                .subTitle(challengeRegisterRequestDto.getSubTitle())
                .category(challengeRegisterRequestDto.getCategory())
                .description(challengeRegisterRequestDto.getDescription())
                .totalScore(challengeRegisterRequestDto.getTotalScore())
                .requiredScore(challengeRegisterRequestDto.getRequiredScore())
                .level(challengeRegisterRequestDto.getLevel())
                .problemCount(challengeRegisterRequestDto.getProblemCount())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        List<LectureDto> lectureDtoList = challengeRegisterRequestDto.getLectureDtoList();

        for (LectureDto lectureDto : lectureDtoList) {
            Lecture lecture = Lecture.builder()
                    .videoUrl(lectureDto.getVideoUrl())
                    .content(lectureDto.getContent())
                    .fileUrl(lectureDto.getFileUrl())
                    .build();

            challenge.addLecture(lecture);
        }

        List<ProblemDto> problemDtoList = challengeRegisterRequestDto.getProblemDtoList();

        for (ProblemDto problemDto : problemDtoList) {
            Problem problem = Problem.builder()
                    .seq(problemDto.getSeq())
                    .content(problemDto.getContent())
                    .isMultiple(problemDto.isMultiple())
                    .answer(problemDto.getAnswer())
                    .multipleAnswer(problemDto.getMultipleAnswer())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            challenge.addProblem(problem);

            List<ChoiceDto> choiceDtoList = problemDto.getChoiceDtoList();
            for (ChoiceDto choiceDto : choiceDtoList) {
                Choice choice = Choice.builder()
                        .seq(choiceDto.getSeq())
                        .content(choiceDto.getContent())
                        .build();
                problem.addChoice(choice);
            }
        }

        Challenge findChallenge = challengeService.registerChallenge(challenge);

        return ApiResult.OK(new ChallengeRegisterResponseDto(findChallenge));
    }

    @GetMapping("possibleList")
    @ApiOperation(value = "도전 가능 챌린지(쿼리 파라미터로 lastChallengeId, size 사용)")
    public ApiResult<List<ChallengeDto>> possibleChallengeList(@AuthenticationPrincipal JwtAuthentication authentication,
                                                     @RequestParam Long lastChallengeId, @RequestParam int size) {
        List<ChallengeDto> challengeDtoList = challengeDtoRepository.findChallenges(authentication.id, lastChallengeId, size);
        return ApiResult.OK(challengeDtoList);
    }




    @PostMapping("{challengeId}/like")
    @ApiOperation(value = "좋아요 기능")
    public ApiResult<Long> like(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable @ApiParam Long challengeId
    ) {
        // 먼저 찾고 없으면 like. 있으면 challengeLike 취소
        Optional<ChallengeLike> challengeLike = challengeLikeService.findChallengeLike(authentication.id, challengeId);
        if (challengeLike.isEmpty()) {
            ChallengeLike saved = challengeLikeService.like(authentication.id, challengeId);
            return ApiResult.OK(saved.getId());
        } else {
            // 삭제했을 때는 0 RETURN
            challengeLikeService.delete(authentication.id, challengeId);
            return ApiResult.OK(0L);
        }

    }

    @GetMapping("likeList")
    @ApiOperation(value = "좋아요 누른 challengeList 불러오기(쿼리 파라미터로 lastChallengeId, size 사용)")
    public ApiResult<List<ChallengeDto>> likeList(
            @AuthenticationPrincipal JwtAuthentication authentication ,
            @RequestParam Long lastChallengeId, @RequestParam int size
    ) {
        List<ChallengeDto> challengeDtoList = new ArrayList<>();
        List<ChallengeLike> challengeLikeList = challengeLikeService.findAll(authentication.id, lastChallengeId, size);
        for (ChallengeLike challengeLike : challengeLikeList) {
            Challenge challenge = challengeLike.getChallenge();
            ChallengeDto challengeDto = new ChallengeDto(challenge.getId(), challenge.getCategory(), challenge.getTitle(),
                    challenge.getSubTitle(),1l,challenge.getTotalScore(),challenge.getRequiredScore());
            challengeDtoList.add(challengeDto);
        }
        return ApiResult.OK(challengeDtoList);
    }

    @GetMapping("completedList")
    @ApiOperation(value = "완료된 챌린지 리스트 반환(쿼리 파라미터로 lastChallengeId, size 사용)")
    public ApiResult<List<MyChallengeDto>> completedList(
            @AuthenticationPrincipal JwtAuthentication authentication ,
            @RequestParam Long lastChallengeId, @RequestParam int size
    ) {
        List<MyChallengeDto> myChallengeDtoList = new ArrayList<>();
        List<ChallengeHistory> challengeHistoryList = challengeService.completedChallengeList(authentication.id, true,lastChallengeId,size);

        for (ChallengeHistory challengeHistory : challengeHistoryList) {
            Challenge challenge = challengeHistory.getChallenge();
            MyChallengeDto myChallengeDto = new MyChallengeDto(challenge.getId(), challenge.getCategory(),
                    challenge.getTitle(), challenge.getSubTitle(), challenge.getTotalScore(),
                    challenge.getRequiredScore(),challengeHistory.getAssignedCandy() ,challengeHistory.isComplete());
            myChallengeDtoList.add(myChallengeDto);
        }

        return ApiResult.OK(myChallengeDtoList);

    }

    @GetMapping("notCompletedList")
    @ApiOperation(value = "완료하지 않은 챌린지 리스트 반환(쿼리 파라미터로 lastChallengeId, size 사용)")
    public ApiResult<List<MyChallengeDto>> notCompletedList(
            @AuthenticationPrincipal JwtAuthentication authentication ,
            @RequestParam Long lastChallengeId, @RequestParam int size
    ) {
        List<MyChallengeDto> myChallengeDtoList = new ArrayList<>();
        List<ChallengeHistory> challengeHistoryList = challengeService.notCompletedChallengeList(authentication.id, false,lastChallengeId,size);

        for (ChallengeHistory challengeHistory : challengeHistoryList) {
            Challenge challenge = challengeHistory.getChallenge();
            MyChallengeDto myChallengeDto = new MyChallengeDto(challenge.getId(), challenge.getCategory(),
                    challenge.getTitle(), challenge.getSubTitle(), challenge.getTotalScore(),
                    challenge.getRequiredScore(),challengeHistory.getAssignedCandy(), challengeHistory.isComplete());
            myChallengeDtoList.add(myChallengeDto);
        }

        return ApiResult.OK(myChallengeDtoList);
    }

    @GetMapping("/{challengeId}/detail")
    @ApiOperation(value = "챌린지 소개 화면 조회")
    public ApiResult<ChallengeDetailResponseDto> challengeDetail(@AuthenticationPrincipal JwtAuthentication authentication,
                                                                 @PathVariable @ApiParam Long challengeId) {
        ChallengeDetailResponseDto challengeDetail = challengeService.findChallengeDetail(authentication.id, challengeId);
        return ApiResult.OK(challengeDetail);
    }
    
    @GetMapping("/problem/list")
    @ApiOperation(value = "문제 반환")
    public ApiResult<List<Problem>> problemList(@AuthenticationPrincipal JwtAuthentication authentication,
            									@PathVariable @ApiParam Long challengeId) {
    	
    	
    	
    	return ApiResult.OK(null);
    }
    
    @GetMapping("/problem/solve")
    @ApiOperation(value = "문제 풀이")
    public ApiResult<List<ProblemSolvingResponseDto>> problemSolving(@AuthenticationPrincipal JwtAuthentication authentication,				
    																 @PathVariable @ApiParam Long challengeId) {
    	
    	return ApiResult.OK(null);
    }
    
    @GetMapping("/problem/marking")
    @ApiOperation(value = "문제 채점")
    public ApiResult<List<ProblemMarkingResponseDto>> problemMarking(@AuthenticationPrincipal JwtAuthentication authentication,
    																 @PathVariable @ApiParam Long challengeId) {
    
    	return ApiResult.OK(null);
    }
}