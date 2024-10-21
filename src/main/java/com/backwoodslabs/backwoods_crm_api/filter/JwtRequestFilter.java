package com.backwoodslabs.backwoods_crm_api.filter;

import com.backwoodslabs.backwoods_crm_api.service.CustomUserDetailsService;
import com.backwoodslabs.backwoods_crm_api.service.JwtTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtTokenService jwtTokenService, @Lazy CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String jwtToken = getJwtFromRequest(request);

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticateWithToken(jwtToken, request);
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    private void authenticateWithToken(String jwtToken, HttpServletRequest request) {
        try {
            String username = jwtTokenService.extractUsername(jwtToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtTokenService.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("JWT token parsing failed: " + e.getMessage());
        }
    }
}
