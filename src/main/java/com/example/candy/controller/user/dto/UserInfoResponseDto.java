package com.example.candy.controller.user.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserInfoResponseDto {
    private String email;
    private String name;
    private String phone;
    private String birth;

    public UserInfoResponseDto(String email, String name, String phone, String birth) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }
}
