package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangeParentPasswordRequestDto {
    @ApiModelProperty(value = "기존 부모 비밀번호", example = "기존  부모 비밀번호")
    private String originParentPassword;
    @ApiModelProperty(value = "새로 설정할 부모 비밀번호", example = "새로 설정할 부모 비밀번호")
    private String newParentPassword;
}
