package com.example.candy.controller.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class ChangeUserInfoRequestDto {
    private String name;
    private String phone;
    private String birth;

    public ChangeUserInfoRequestDto(String name, String phone, String birth) {
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }
}
