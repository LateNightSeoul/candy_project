package com.example.candy.controller.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class UserInfoResponseDto {
    @ApiModelProperty
    private String email;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private String phone;
    @ApiModelProperty
    private String birth;

    public UserInfoResponseDto() {}

    public UserInfoResponseDto(String email, String name, String phone, String birth) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }
}
