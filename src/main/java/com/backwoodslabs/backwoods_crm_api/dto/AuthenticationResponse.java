package com.backwoodslabs.backwoods_crm_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private Long id;
}
