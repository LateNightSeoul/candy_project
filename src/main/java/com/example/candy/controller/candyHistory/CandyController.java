package com.example.candy.controller.candyHistory;

import com.example.candy.controller.ApiResult;
import com.example.candy.controller.candyHistory.dto.*;
import com.example.candy.security.JwtAuthentication;
import com.example.candy.service.candyHistory.CandyHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candy")
@RequiredArgsConstructor
@Api(tags = {"캔디"})
public class CandyController {

    private final CandyHistoryService candyHistoryService;

    @GetMapping("/student")
    @ApiOperation(value = "학생 캔디 조회")
    public ApiResult<CandyResponseDto> candyStudent(@AuthenticationPrincipal JwtAuthentication authentication) {
        System.out.println(authentication);
        return ApiResult.OK(new CandyResponseDto(candyHistoryService.candyStudent(authentication.id)));
    }

    @GetMapping("/parent")
    @ApiOperation(value = "학부모 캔디 조회")
    public ApiResult<CandyResponseDto> candyParent(@AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiResult.OK(new CandyResponseDto(candyHistoryService.candyParent(authentication.id)));
    }

    @PostMapping("/charge")
    @ApiOperation(value = "캔디 충전")
    public ApiResult<CandyResponseDto> chargeCandy(@AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody @ApiParam CandyChargeRequestDto candyChargeRequestDto) {
        return ApiResult.OK(new CandyResponseDto(
                candyHistoryService.chargeCandy(authentication.id,
                candyChargeRequestDto.getAmount()).getParentCandy()));
    }

    @PostMapping("/withdraw")
    @ApiOperation(value = "캔디 인출")
    public ApiResult<CandyWithdrawResponseDto> withdrawCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                                             @RequestBody @ApiParam CandyWithdrawRequestDto candyWithdrawRequestDto) {
        return ApiResult.OK(new CandyWithdrawResponseDto(
                candyHistoryService.withdrawCandy(authentication.id, candyWithdrawRequestDto.getAmount()).getStudentCandy(),
                candyWithdrawRequestDto.getAmount())
        );
    }

    @PostMapping("/assign")
    @ApiOperation(value = "캔디 배정 (부모 캔디를 챌린지에 배정)")
    public ApiResult assignCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                 @RequestBody @ApiParam CandyAssignRequestDto candyAssignRequestDto) {
        candyHistoryService.assignCandy(authentication.id,
                candyAssignRequestDto.getChallengeId(), candyAssignRequestDto.getCandyAmount());
        return ApiResult.OK(null);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "캔디 배정 취소 (챌린지에 배정된 캔디 취소)")
    public ApiResult cancelCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                 @RequestBody @ApiParam CandyCancelRequestDto candyCancelRequestDto) {
        candyHistoryService.cancelCandy(authentication.id, candyCancelRequestDto.getChallengeId());
        return ApiResult.OK(null);
    }

    @PostMapping("/attain")
    @ApiOperation(value = "캔디 획득 (챌린지 성공 후 챌린지에 배정된 캔디 -> 학생 캔디로 획득), 챌린지 성공 처리도 같이 함")
    public ApiResult attainCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                 @RequestBody @ApiParam CandyAttainRequestDto candyAttainRequestDto) {
        candyHistoryService.attainCandy(authentication.id, candyAttainRequestDto.getChallengeId());
        return ApiResult.OK(null);
    }
}
