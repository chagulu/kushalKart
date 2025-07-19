package com.kushalkart.controller;

import com.kushalkart.dto.UserDTO;
import com.kushalkart.dto.UserAddressDTO;
import com.kushalkart.entity.User;
import com.kushalkart.entity.UserAddress;
import com.kushalkart.repository.UserAddressRepository;
import com.kushalkart.repository.UserRepository;
import com.kushalkart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          UserAddressRepository userAddressRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody UserDTO userDTO) {
        return userService.sendOtp(userDTO.getMobile());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return userService.loginWithOtp(userDTO.getMobile(), userDTO.getOtp());
    }

    @PatchMapping("/{userId}/address")
    public ResponseEntity<?> updateUserAddress(
            @PathVariable Long userId,
            @RequestBody UserAddressDTO request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserAddress address = userAddressRepository.findByUserId(userId)
                .orElse(new UserAddress());

        address.setUser(user);
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLocationLat(request.getLocationLat());
        address.setLocationLng(request.getLocationLng());

        userAddressRepository.save(address);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User address updated successfully"
        ));
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}
