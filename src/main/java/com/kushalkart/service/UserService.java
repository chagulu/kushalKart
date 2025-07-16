package com.kushalkart.service;

import com.kushalkart.entity.User;
import com.kushalkart.repository.UserRepository;
import com.kushalkart.util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    /**
     * Send or Resend OTP to the given mobile number.
     * If user does not exist, create a new user.
     * If user already exists, just generate and save a new OTP.
     */
    public ResponseEntity<?> sendOtp(String mobile) {
        String otp = generateOtp();

        User user = userRepository.findByMobile(mobile)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setMobile(mobile);
                    newUser.setVerified(false);
                    return newUser;
                });

        user.setOtp(otp);

        // Optionally, track when OTP was sent
        user.setOtpGeneratedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message", "OTP sent to mobile",
                        "mobile", mobile,
                        "otp", otp // ⚠️ For dev/debug only. Remove in production.
                )
        );
    }

    /**
     * Verify OTP and return JWT token if successful.
     */
    public ResponseEntity<?> loginWithOtp(String mobile, String otp) {
        return userRepository.findByMobile(mobile).map(user -> {
            if (user.getOtp().equals(otp)) {
                user.setVerified(true);
                userRepository.save(user);

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

    /**
     * Generates a 6-digit OTP.
     */
    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
