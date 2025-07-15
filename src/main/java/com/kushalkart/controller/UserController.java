package com.kushalkart.controller;

import com.kushalkart.dto.UserDTO;
import com.kushalkart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Send OTP to the mobile number.
     * If user does not exist, create one.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody UserDTO userDTO) {
        return userService.sendOtp(userDTO.getMobile());
    }

    /**
     * Verify OTP and login — return JWT token & user info.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return userService.loginWithOtp(userDTO.getMobile(), userDTO.getOtp());
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
