package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangePasswordRequestDto {
    @ApiModelProperty(value = "기존 비밀번호", example = "기존 비밀번호")
    private String originPassword;
    @ApiModelProperty(value = "새로 설정할 비밀번호", example = "새로 설정할 비밀번호")
    private String newPassword;
}
