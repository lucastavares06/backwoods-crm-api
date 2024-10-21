package com.backwoodslabs.backwoods_crm_api.controller;

import com.backwoodslabs.backwoods_crm_api.dto.RegisterUserDto;
import com.backwoodslabs.backwoods_crm_api.model.User;
import com.backwoodslabs.backwoods_crm_api.service.EmailVerificationService;
import com.backwoodslabs.backwoods_crm_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @Operation(
            summary = "Register a new user",
            description = "Endpoint for registering a new user. Accepts user details in the request body and sends a verification email to the user."
    )
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto, HttpServletRequest request) {
        try {
            User newUser = userService.registerUser(registerUserDto);

            sendVerificationEmail(newUser, request);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with ID: " + newUser.getId());
        } catch (RuntimeException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    private void sendVerificationEmail(User user, HttpServletRequest request) throws MessagingException {
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
        emailVerificationService.handleUserEmailVerificationToken(user, siteURL);
    }
}
