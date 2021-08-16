package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class ChangeUserInfoRequestDto {
    @ApiModelProperty(value = "이름", example = "이해석")
    private String name;
    @ApiModelProperty(value = "전화번호", example = "01012345678")
    private String phone;
    @ApiModelProperty(value = "생년월일", example = "19950302")
    private String birth;

    public ChangeUserInfoRequestDto(String name, String phone, String birth) {
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }
}
