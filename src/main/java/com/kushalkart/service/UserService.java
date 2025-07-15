package com.kushalkart.service;

import com.kushalkart.entity.User;
import com.kushalkart.repository.UserRepository;
import com.kushalkart.util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * Send OTP to mobile number.
     * If user does not exist, create it.
     */
    public ResponseEntity<?> sendOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);

        User user = userRepository.findByMobile(mobile)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setMobile(mobile);
                    newUser.setVerified(false);
                    return newUser;
                });

        user.setOtp(otp);
        userRepository.save(user);

        // In real world — send OTP via SMS gateway
        return ResponseEntity.ok(Map.of(
                "message", "OTP sent to mobile",
                "mobile", mobile,
                "otp", otp // ⚠️ for dev/debug only. Remove in production.
        ));
    }

    /**
     * Verify OTP & login.
     * Return JWT token and user details if valid.
     */
    public ResponseEntity<?> loginWithOtp(String mobile, String otp) {
        return userRepository.findByMobile(mobile).map(user -> {
            if (user.getOtp().equals(otp)) {
                user.setVerified(true);
                userRepository.save(user);

                String token = jwtService.generateToken(user);

                return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "token", token,
                        "user", Map.of(
                                "id", user.getId(),
                                "mobile", user.getMobile()
                        )
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Invalid OTP"
                ));
            }
        }).orElse(ResponseEntity.badRequest().body(Map.of(
                "error", "User not found"
        )));
    }
}
