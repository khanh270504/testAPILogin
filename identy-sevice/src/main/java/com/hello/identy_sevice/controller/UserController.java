package com.hello.identy_sevice.controller;

import com.hello.identy_sevice.dto.request.ApiResponse;
import com.hello.identy_sevice.dto.request.UserCreationRequest;
import com.hello.identy_sevice.dto.request.UserUpdateRequest;
import com.hello.identy_sevice.dto.response.UserResponse;
import com.hello.identy_sevice.entity.User;
import com.hello.identy_sevice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

     UserService userService;

    @GetMapping("")
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @PostMapping("")
    ApiResponse<UserResponse> addUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createRequest(request));

        return  apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable("id") String id) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
        .build();
    }
    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable("id") String id, @RequestBody UserUpdateRequest request) {
        System.out.println("Received update request for ID: " + id);
        System.out.println("Request body: " + request);
        return  ApiResponse.<UserResponse>builder()
                .result(userService.UpdateRequest(id, request))
                .build();

    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }
}
