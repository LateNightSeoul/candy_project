package com.example.candy.controller.candyHistory;

import com.example.candy.controller.ApiResult;
import com.example.candy.security.JwtAuthentication;
import com.example.candy.service.candyHistory.CandyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candy")
@RequiredArgsConstructor
public class CandyController {

    private final CandyHistoryService candyHistoryService;

    @GetMapping("/student")
    public ApiResult<CandyResponseDto> candyStudent(@AuthenticationPrincipal JwtAuthentication authentication) {
        System.out.println(authentication);
        return ApiResult.OK(new CandyResponseDto(candyHistoryService.candyStudent(authentication.id)));
    }

    @GetMapping("/parent")
    public ApiResult<CandyResponseDto> candyParent(@AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiResult.OK(new CandyResponseDto(candyHistoryService.candyParent(authentication.id)));
    }

    @PostMapping("/charge")
    public ApiResult<CandyResponseDto> chargeCandy(@AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody CandyChargeRequestDto candyChargeRequestDto) {
        return ApiResult.OK(new CandyResponseDto(
                candyHistoryService.chargeCandy(authentication.id,
                candyChargeRequestDto.getAmount()).getParentCandy()));
    }

    @PostMapping("/withdraw")
    public ApiResult<CandyWithdrawResponseDto> withdrawCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                                             @RequestBody CandyWithdrawRequestDto candyWithdrawRequestDto) {
        return ApiResult.OK(new CandyWithdrawResponseDto(
                candyHistoryService.withdrawCandy(authentication.id, candyWithdrawRequestDto.getAmount()).getStudentCandy(),
                candyWithdrawRequestDto.getAmount())
        );
    }

    @PostMapping("/assign")
    public ApiResult assignCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                 @RequestBody CandyAssignRequestDto candyAssignRequestDto) {
        candyHistoryService.assignCandy(authentication.id,
                candyAssignRequestDto.getChallengeId(), candyAssignRequestDto.getCandyAmount());
        return ApiResult.OK(null);
    }

    @PostMapping("/cancel")
    public ApiResult cancelCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                 @RequestBody CandyCancelRequestDto candyCancelRequestDto) {
        candyHistoryService.cancelCandy(authentication.id, candyCancelRequestDto.getChallengeId());
        return ApiResult.OK(null);
    }

    @PostMapping("/attain")
    public ApiResult attainCandy(@AuthenticationPrincipal JwtAuthentication authentication,
                                 @RequestBody CandyAttainRequestDto candyAttainRequestDto) {
        candyHistoryService.attainCandy(authentication.id, candyAttainRequestDto.getChallengeId());
        return ApiResult.OK(null);
    }
}
