package com.hello.identy_sevice.dto.request;

import com.hello.identy_sevice.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 6, message = "USERNAME_INVALID")
     String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
     String password;
     String firstName;
     String lastName;
    @DobConstraint(min = 18, message = "INVALID_DOB")
     LocalDate dob;
    }
