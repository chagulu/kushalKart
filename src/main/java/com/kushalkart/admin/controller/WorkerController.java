package com.kushalkart.admin.controller;

import com.kushalkart.admin.dto.WorkerRegisterRequest;
import com.kushalkart.admin.entity.Worker;
import com.kushalkart.admin.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerWorker(@RequestBody WorkerRegisterRequest request) {

        if (workerRepository.findByMobile(request.getMobile()).isPresent()) {
            return buildErrorResponse("Mobile number already exists");
        }

        if (workerRepository.findByUsername(request.getUsername()).isPresent()) {
            return buildErrorResponse("Username already exists");
        }

        Worker worker = new Worker();
        worker.setName(request.getName());
        worker.setEmail(request.getEmail());
        worker.setMobile(request.getMobile());
        worker.setUsername(request.getUsername());
        worker.setPassword(passwordEncoder.encode(request.getPassword()));
        worker.setServiceCategoryId(request.getServiceCategoryId());
        worker.setBio(request.getBio());
        worker.setSkills(request.getSkillsJson());
        worker.setRatePerHour(request.getRatePerHour());
        worker.setVerified(false);
        worker.setKycStatus(Worker.KycStatus.PENDING);

        workerRepository.save(worker);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Worker registered successfully");

        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return ResponseEntity.badRequest().body(response);
    }
}
