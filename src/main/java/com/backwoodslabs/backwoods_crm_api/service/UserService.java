package com.backwoodslabs.backwoods_crm_api.service;

import com.backwoodslabs.backwoods_crm_api.dto.RegisterUserDto;
import com.backwoodslabs.backwoods_crm_api.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    User registerUser(RegisterUserDto registerUserDto);
    User authenticateUser(String username, String password);
    UserDetails createUserDetails(User user);
}
