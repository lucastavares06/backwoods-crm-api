package com.backwoodslabs.backwoods_crm_api.service;

import com.backwoodslabs.backwoods_crm_api.model.ERole;
import com.backwoodslabs.backwoods_crm_api.model.Role;
import com.backwoodslabs.backwoods_crm_api.model.User;
import com.backwoodslabs.backwoods_crm_api.repository.RoleRepository;
import com.backwoodslabs.backwoods_crm_api.repository.UserRepository;
import com.backwoodslabs.backwoods_crm_api.dto.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new RuntimeException("Error: Username is already in use!");
        }

        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
            throw new RuntimeException("Error: Password and Confirm Password do not match.");
        }

        User newUser = new User();
        newUser.setUsername(registerUserDto.getUsername());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        newUser.setFullName(registerUserDto.getFullName());
        newUser.setPhoneNumber(registerUserDto.getPhoneNumber());
        newUser.setEmailVerified(false);

        Set<Role> roles = assignDefaultRole();
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

    public UserDetails createUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
        );
    }

    private Set<Role> assignDefaultRole() {
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(ERole.valueOf(DEFAULT_ROLE));
        if (userRole.isEmpty()) {
            throw new RuntimeException("Error: Default user role not found.");
        }
        roles.add(userRole.get());
        return roles;
    }

    @Override
    public User authenticateUser(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email before logging in.");
        }

        return user;
    }
}
