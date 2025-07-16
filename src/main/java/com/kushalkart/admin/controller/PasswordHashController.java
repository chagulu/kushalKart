package com.kushalkart.admin.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/superadmin")
public class PasswordHashController {

    private final PasswordEncoder passwordEncoder;

    public PasswordHashController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hash")
    public String hashPassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
}
