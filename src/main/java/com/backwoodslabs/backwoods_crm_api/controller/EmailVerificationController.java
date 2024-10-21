package com.backwoodslabs.backwoods_crm_api.controller;

import com.backwoodslabs.backwoods_crm_api.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verify-email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(
            summary = "Verify user's email",
            description = "Endpoint for email verification. Takes a verification token as a request parameter and verifies the user's email if the token is valid."
    )
    @GetMapping
    public ResponseEntity<String> verifyEmail(
            @Parameter(description = "Verification token sent to the user's email") @RequestParam("token") String token) {
        boolean isVerified = emailVerificationService.verifyUser(token);

        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification token.");
        }
    }
}
