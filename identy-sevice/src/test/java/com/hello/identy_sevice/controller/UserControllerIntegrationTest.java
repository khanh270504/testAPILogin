package com.hello.identy_sevice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hello.identy_sevice.dto.request.UserCreationRequest;
import com.hello.identy_sevice.dto.response.UserResponse;
import com.hello.identy_sevice.exception.GlobalExceptionHandler;
import com.hello.identy_sevice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.33");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");

    }


    @Autowired
    private MockMvc mockMvc;



    private ObjectMapper objectMapper;
    private UserCreationRequest validRequest;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void setUp() {

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

        String content = objectMapper.writeValueAsString(validRequest);

        var response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.username").value("john2705")
                );

        log.info("Result: {}", response.andReturn().getResponse().getContentAsString());
    }
//
//    @Test
//        void createUser_usernameInvalid_fail() throws Exception {
//            // Username < 6 ký tự để trigger validation
//            validRequest.setUsername("abc");
//        String content = objectMapper.writeValueAsString(validRequest);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andExpect(status().isBadRequest());
////                .andExpect(jsonPath("code")
////                        .value(1003))
////                .andExpect(jsonPath("message")
////                        .value("Username must be at least 6 characters"));
//    }
}
