package com.backwoodslabs.backwoods_crm_api.controller;

import com.backwoodslabs.backwoods_crm_api.dto.AuthenticationResponse;
import com.backwoodslabs.backwoods_crm_api.dto.LoginUserDto;
import com.backwoodslabs.backwoods_crm_api.model.User;
import com.backwoodslabs.backwoods_crm_api.service.JwtTokenService;
import com.backwoodslabs.backwoods_crm_api.service.LogoutService;
import com.backwoodslabs.backwoods_crm_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final LogoutService logoutService;

    @Operation(
            summary = "Authenticate user and generate a JWT token",
            description = "Endpoint for user authentication. Takes a username and password, and if the credentials are correct, returns a JWT token."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        User user = userService.authenticateUser(loginUserDto.getUsername(), loginUserDto.getPassword());

        UserDetails userDetails = userService.createUserDetails(user);

        String jwtToken = jwtTokenService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(
                jwtToken,
                "Bearer",
                user.getUsername(),
                user.getEmail(),
                user.getId()
        ));
    }

    @Operation(summary = "Test if JWT is valid", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Token is valid!");
    }

    @Operation(summary = "Logout the authenticated user", security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, Authentication authentication) {
        logoutService.performLogout(request, null, authentication);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
