package com.hello.identy_sevice.service;

import com.hello.identy_sevice.dto.request.UserCreationRequest;
import com.hello.identy_sevice.dto.response.UserResponse;
import com.hello.identy_sevice.entity.User;
import com.hello.identy_sevice.exception.AppException;
import com.hello.identy_sevice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        user = User.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createRequest(request);
        // THEN

        Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
    }

    @Test
    void createUser_userExisted_fail(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createRequest(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1002);
    }
}