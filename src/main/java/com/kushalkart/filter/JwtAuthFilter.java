package com.kushalkart.filter;

import com.kushalkart.util.JwtService;
import com.kushalkart.admin.service.AdminUserDetailsService;
import com.kushalkart.service.MyUserDetailsService;
import com.kushalkart.model.CustomUserDetails;
import com.kushalkart.entity.User;
import com.kushalkart.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt); // usually mobile

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

            UserDetails userDetails;

            if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
                userDetails = adminUserDetailsService.loadUserByUsername(username);
            } else {
                User user = userRepository.findByMobile(username).orElse(null);
                if (user == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                userDetails = new CustomUserDetails(user);
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                setAuthentication(userDetails, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
