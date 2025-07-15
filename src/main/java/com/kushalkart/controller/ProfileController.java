package com.kushalkart.controller;

import com.kushalkart.entity.User;
import com.kushalkart.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.kushalkart.dto.UpdateProfileRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || userDetails.getUsername() == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Unauthorized: No authenticated user found"));
        }

        return userRepository.findByMobile(userDetails.getUsername())
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", user.getId());
                    response.put("mobile", user.getMobile());
                    response.put("verified", user.isVerified());
                    // if you have more fields (like name, email), you can add them here
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity
                        .status(404)
                        .body(Map.of("error", "User not found in database")));
    }

    @PutMapping("/profile")
public ResponseEntity<?> updateProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody UpdateProfileRequest request
) {
    if (userDetails == null) {
        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
    }

    User user = userRepository.findByMobile(userDetails.getUsername())
            .orElse(null);

    if (user == null) {
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    }

    // update fields
    if (request.getName() != null) {
        user.setName(request.getName());
    }
    if (request.getEmail() != null) {
        user.setEmail(request.getEmail());
    }

    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
            "message", "Profile updated successfully",
            "id", user.getId(),
            "mobile", user.getMobile(),
            "name", user.getName(),
            "email", user.getEmail()
    ));
}
}
