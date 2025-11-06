package com.hello.identy_sevice.controller;


import com.hello.identy_sevice.dto.request.ApiResponse;
import com.hello.identy_sevice.dto.request.AuthenticationRequest;
import com.hello.identy_sevice.dto.request.IntrospectRequest;
import com.hello.identy_sevice.dto.response.AuthenticationResponse;
import com.hello.identy_sevice.dto.response.IntrospectResponse;
import com.hello.identy_sevice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        var authenticate = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticate).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectResponse(@RequestBody IntrospectRequest request)
    throws ParseException, JOSEException {
        var result = authenticationService.introspectResponse(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result).build();
    }

}
