package com.kushalkart.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushalkart.util.JwtService;
import com.kushalkart.admin.service.AdminUserDetailsService;
import com.kushalkart.service.MyUserDetailsService;
import com.kushalkart.model.CustomUserDetails;
import com.kushalkart.entity.User;
import com.kushalkart.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        try {
            username = jwtService.extractUsername(jwt);

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

        } catch (ExpiredJwtException e) {
            handleJwtException(response, "JWT token has expired", "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED);
            return;
        } catch (SignatureException e) {
            handleJwtException(response, "JWT signature is invalid", "INVALID_SIGNATURE", HttpStatus.FORBIDDEN);
            return;
        } catch (MalformedJwtException e) {
            handleJwtException(response, "JWT token is malformed", "MALFORMED_TOKEN", HttpStatus.BAD_REQUEST);
            return;
        } catch (Exception e) {
            handleJwtException(response, "JWT authentication failed", "AUTH_FAILED", HttpStatus.UNAUTHORIZED);
            return;
        }
    }

    private void handleJwtException(HttpServletResponse response, String message, String errorCode, HttpStatus status) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("message", message);
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("status", status.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
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
