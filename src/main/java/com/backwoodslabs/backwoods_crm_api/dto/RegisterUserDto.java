package com.backwoodslabs.backwoods_crm_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {

    @NotBlank(message = "Username is mandatory")
    @Size(max = 20, message = "Username must be up to 20 characters long")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must be up to 50 characters long")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 120, message = "Password must be between 8 and 120 characters")
    private String password;

    @NotBlank(message = "Confirm Password is mandatory")
    @Size(min = 8, max = 120, message = "Confirm Password must be between 8 and 120 characters")
    private String confirmPassword;

    @NotBlank(message = "Full name is mandatory")
    @Size(max = 100, message = "Full name must be up to 100 characters long")
    private String fullName;

    @Size(max = 15, message = "Phone number must be up to 15 characters long")
    private String phoneNumber;
}
