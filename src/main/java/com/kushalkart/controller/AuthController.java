package com.kushalkart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // You can also optionally invalidate token in DB if you’re tracking
        return ResponseEntity.ok(Map.of(
                "message", "Logout successful. Please remove token from client."
        ));
    }
}
