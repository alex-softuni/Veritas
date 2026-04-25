package com.example.veritas.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 6, message = "Username must be at least 6 symbols" )
    private String username;

    @Size(min = 6, message = "Username must be at least 6 symbols" )
    private String password;

    @Email(message = "Please enter a valid email address")
    private String email;

}
