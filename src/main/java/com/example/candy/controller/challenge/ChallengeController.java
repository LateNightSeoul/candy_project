package com.example.candy.controller.challenge;

import com.example.candy.controller.ApiResult;
import com.example.candy.controller.challenge.dto.*;
import com.example.candy.domain.challenge.Challenge;
import com.example.candy.domain.challenge.ChallengeHistory;
import com.example.candy.domain.challenge.ChallengeLike;
import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.lecture.Lecture;
import com.example.candy.domain.problem.Problem;
import com.example.candy.domain.problem.ProblemHistory;
import com.example.candy.enums.Category;
import com.example.candy.repository.challenge.ChallengeDtoRepository;
import com.example.candy.security.JwtAuthentication;
import com.example.candy.service.challenge.ChallengeLikeService;
import com.example.candy.service.challenge.ChallengeService;
import com.example.candy.service.storage.FileStorageService;
import com.example.candy.service.stream.VideoStreamingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private VideoStreamingService videoStreamingService;
    

    @GetMapping("category")
    @ApiOperation(value = "category list")
    public Category[] categoryList(){
        Category[] categories = Category.values();
        return categories;
    }

    @PostMapping("/register")
    @ApiOperation(value = "챌린지 등록")
    public ApiResult<ChallengeRegisterResponseDto> register(@RequestBody @ApiParam ChallengeRegisterRequestDto challengeRegisterRequestDto) {

        // challenge 생성
        Challenge challenge = Challenge.create(challengeRegisterRequestDto);

        // lecture 생성
        List<LectureDto> lectureDtoList = challengeRegisterRequestDto.getLectureDtoList();
        for (LectureDto lectureDto : lectureDtoList) {
            Lecture lecture = Lecture.create(lectureDto);
            challenge.addLecture(lecture);
        }

        // problem 생성
        List<ProblemDto> problemDtoList = challengeRegisterRequestDto.getProblemDtoList();
        for (ProblemDto problemDto : problemDtoList) {
            Problem problem = Problem.create(problemDto);
            challenge.addProblem(problem);

            // choice 생성
            List<ChoiceDto> choiceDtoList = problemDto.getChoiceDtoList();
            for (ChoiceDto choiceDto : choiceDtoList) {
                Choice choice = Choice.create(choiceDto);
                problem.addChoice(choice);
            }
        }

        // challenge db에 등록
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

        completeMyChallengeDtoList(myChallengeDtoList, challengeHistoryList);
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
        completeMyChallengeDtoList(myChallengeDtoList, challengeHistoryList);

        return ApiResult.OK(myChallengeDtoList);
    }

    private void completeMyChallengeDtoList(List<MyChallengeDto> myChallengeDtoList ,List<ChallengeHistory> challengeHistoryList) {
        for (ChallengeHistory challengeHistory : challengeHistoryList) {
            Challenge challenge = challengeHistory.getChallenge();
            MyChallengeDto myChallengeDto = createMyChallengeDto(challenge, challengeHistory);
            myChallengeDtoList.add(myChallengeDto);
        }
    }

    private MyChallengeDto createMyChallengeDto(Challenge challenge, ChallengeHistory challengeHistory) {
        return new MyChallengeDto(challenge.getId(), challenge.getLectures(), challenge.getCategory(),
                challenge.getTitle(), challenge.getSubTitle(), challenge.getTotalScore(),
                challenge.getRequiredScore(),challengeHistory.getAssignedCandy() ,challengeHistory.isComplete());
    }

    @GetMapping("/{challengeId}/detail")
    @ApiOperation(value = "챌린지 소개 화면 조회")
    public ApiResult<ChallengeDetailResponseDto> challengeDetail(@AuthenticationPrincipal JwtAuthentication authentication,
                                                                 @PathVariable @ApiParam Long challengeId) {
        ChallengeDetailResponseDto challengeDetail = challengeService.findChallengeDetail(authentication.id, challengeId);
        return ApiResult.OK(challengeDetail);
    }

    @GetMapping("/score/{challengeId}")
    @ApiOperation(value = "챌린지 점수 조회 (역대 기록 중 가장 높은 점수 반환)")
    public ApiResult<ChallengeScoreResponseDto> getScore(@AuthenticationPrincipal JwtAuthentication authentication,
                                @PathVariable @ApiParam Long challengeId) {
        int score = challengeService.findScore(challengeId, authentication.id);
        return ApiResult.OK(new ChallengeScoreResponseDto(score));
    }
    
    @PostMapping("/problem/solve")
    @ApiOperation(value = "문제 풀이")
    public ApiResult<ProblemSolvingResponseDto> problemSolving(@AuthenticationPrincipal JwtAuthentication authentication,
    		@RequestBody @ApiParam(required = true) ProblemSolvedRequestDtoList problemSolvedRequestDtoList) {
        int totalScore = challengeService.grading(authentication.id, problemSolvedRequestDtoList);

        return ApiResult.OK(new ProblemSolvingResponseDto(authentication.id, totalScore));
    }
    
    @PostMapping("/problem/return")
    @ApiOperation(value = "문제 반환")
    public ApiResult<ProblemResponseDtoList> problemReturning(@AuthenticationPrincipal JwtAuthentication authentication,
    		@RequestBody @ApiParam(required = true) ProblemRequestDto problemRequestDto) {

        Challenge challenge = challengeService.findChallenge(problemRequestDto.getChallengeId());
        List<ProblemResponseDto> problemResponseDto = new ArrayList<ProblemResponseDto>();

    	for (Problem problem : challenge.getProblems()) {
            List<ChoiceDto> choiceDto = new ArrayList<ChoiceDto>();
            for (Choice choice : problem.getChoices()) {
                choiceDto.add(new ChoiceDto(choice.getSeq(), choice.getContent()));
            }
            problemResponseDto.add(new ProblemResponseDto(choiceDto, problem.getId(), problem.getSeq(), problem.getQuestion(), problem.getContent(), problem.getScore(), problem.isMultiple(), problem.getMultipleAnswer(), problem.getAnswer(), problem.getMultipleCount(), problem.getModifiedDate()));
    	}

    	return ApiResult.OK(new ProblemResponseDtoList(problemResponseDto));
    }
    
    
    @PostMapping("/video/lecture/upload")
    @ApiOperation(value = "강의 업로드")
    public ApiResult<List<VideoLectureUploadingResponseDto>> videoLecturesRegister(@AuthenticationPrincipal JwtAuthentication authentication,
    		@RequestParam("file") @ApiParam(required = true) MultipartFile[] files) {
    	ArrayList<VideoLectureUploadingResponseDto> videoLectureUploadingResponseDto = new ArrayList<VideoLectureUploadingResponseDto>();

    	for (MultipartFile file : files) {
    		String videoUrl = fileStorageService.storeFile(file);
    		videoLectureUploadingResponseDto.add(new VideoLectureUploadingResponseDto(videoUrl));
    	}
    	
    	return ApiResult.OK(videoLectureUploadingResponseDto);
    }

    @PostMapping("/video/lecture/check")
    @ApiOperation(value = "강의 조회")
    public VideoLectureCheckResponseDto getVideoUrl(@RequestBody @ApiParam(required = true) VideoLectureCheckRequestDto videoLectureCheckRequestDto) throws IOException {
    	Challenge challenge = challengeService.findChallenge(videoLectureCheckRequestDto.getChallengeId());
    	List<Lecture> lectures = challenge.getLectures();
        List<String> lecturesUrl = new ArrayList<>();
        for (Lecture lecture : lectures)
            lecturesUrl.add(lecture.getVideoUrl());
    	
    	return new VideoLectureCheckResponseDto(lecturesUrl);
    }
    
    @GetMapping("/video/lecture/view")
    @ApiOperation(value = "강의 보기")
    public ResponseEntity<ResourceRegion> getVideo( 
    		@RequestHeader(value = "Range", required = false) String rangeHeader, @RequestParam @ApiParam(required = true) String video_url) throws IOException {
    	
    	return videoStreamingService.getVideoRegion(rangeHeader, "/Users/hexk0131/lecture/", video_url);
    }
}