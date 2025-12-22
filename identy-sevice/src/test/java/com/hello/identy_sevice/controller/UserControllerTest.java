package com.hello.identy_sevice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hello.identy_sevice.dto.request.UserCreationRequest;
import com.hello.identy_sevice.dto.response.UserResponse;
import com.hello.identy_sevice.exception.GlobalExceptionHandler;
import com.hello.identy_sevice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private UserCreationRequest validRequest;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void setUp() {
        // MockMvc standalone (không load Spring context)
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // ObjectMapper khởi tạo 1 lần
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        dob = LocalDate.of(1990, 1, 1);

        validRequest = UserCreationRequest.builder()
                .username("john2705")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("john2705")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // Mock service trả về userResponse
        when(userService.createRequest(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        String content = objectMapper.writeValueAsString(validRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.id").value("cf0600f538b3"));
    }

    @Test
        void createUser_usernameInvalid_fail() throws Exception {
            // Username < 6 ký tự để trigger validation
            validRequest.setUsername("abc");
        String content = objectMapper.writeValueAsString(validRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("code")
//                        .value(1003))
//                .andExpect(jsonPath("message")
//                        .value("Username must be at least 6 characters"));
    }
}
