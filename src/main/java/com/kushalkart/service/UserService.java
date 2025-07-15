package com.kushalkart.service;

import com.kushalkart.entity.User;
import com.kushalkart.repository.UserRepository;
import com.kushalkart.util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
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

        return ResponseEntity.ok(
                Map.of(
                        "message", "OTP sent to mobile",
                        "mobile", mobile,
                        "otp", otp // ⚠️ Remove in production
                )
        );
    }

    public ResponseEntity<?> loginWithOtp(String mobile, String otp) {
        return userRepository.findByMobile(mobile).map(user -> {
            if (user.getOtp().equals(otp)) {
                user.setVerified(true);
                userRepository.save(user);

                // Option 1: Wrap into UserDetails
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getMobile(), "", List.of()
                );

                String token = jwtService.generateToken(userDetails);

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
