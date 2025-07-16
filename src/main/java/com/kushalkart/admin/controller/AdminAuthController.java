package com.kushalkart.admin.controller;

import com.kushalkart.admin.entity.AdminUser;
import com.kushalkart.admin.repository.AdminUserRepository;
import com.kushalkart.admin.util.AdminJwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final AuthenticationManager authManager;
    private final AdminJwtService jwtService;
    private final AdminUserRepository userRepo;

    public AdminAuthController(AuthenticationManager authManager, AdminJwtService jwtService, AdminUserRepository userRepo) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        AdminUser user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token, user));
    }

    // DTO Classes (you can move them to separate files if preferred)
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String token;
        private AdminUser user;

        public LoginResponse(String token, AdminUser user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public AdminUser getUser() {
            return user;
        }
    }
}
