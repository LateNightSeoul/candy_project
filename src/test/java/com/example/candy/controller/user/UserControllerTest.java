package com.example.candy.controller.user;

import com.example.candy.domain.user.User;
import com.example.candy.repository.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이메일 중복 확인")
    void checkEmail() throws Exception {
        Map<String, String> email = new HashMap<>();
        email.put("email", "a@naver.com");

        ResultActions result = mockMvc.perform(
                post("/user/email/exist")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(email))
                    .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(false)));

        User user = User.builder()
                .email("a@naver.com")
                .build();
        userRepository.save(user);

        ResultActions result2 = mockMvc.perform(
                post("/user/email/exist")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(email))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result2.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    void 회원가입_이메일_실패() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("email", "leehae8");
        data.put("emailcheck", Boolean.valueOf(true));
        data.put("password", "123456789");
        data.put("parentPassword", "123456789");
        data.put("name", "이해석");
        data.put("phone", "010-5612-2134");
        data.put("birth", "1995-03-02");
        ResultActions resultActions = mockMvc.perform(
                post("/user/join")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        resultActions.andDo(print());
    }
}