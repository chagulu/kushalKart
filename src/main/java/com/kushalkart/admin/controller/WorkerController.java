package com.kushalkart.admin.controller;

import com.kushalkart.admin.dto.WorkerRegisterRequest;
import com.kushalkart.admin.entity.Worker;
import com.kushalkart.admin.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerWorker(@RequestBody WorkerRegisterRequest request) {

        // check duplicate mobile
        if (workerRepository.findByMobile(request.getMobile()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Mobile number already exists");
        }

        Worker worker = new Worker();
        worker.setName(request.getName());
        worker.setEmail(request.getEmail());
        worker.setMobile(request.getMobile());
        worker.setUsername(request.getUsername());
        worker.setPassword(passwordEncoder.encode(request.getPassword()));
        worker.setServiceCategoryId(request.getServiceCategoryId());
        worker.setBio(request.getBio());
        worker.setSkills(request.getSkillsJson()); // if JSON string
        worker.setRatePerHour(request.getRatePerHour());
        worker.setVerified(false);
        worker.setKycStatus(Worker.KycStatus.PENDING);

        workerRepository.save(worker);

        return ResponseEntity.ok("Worker registered successfully");
    }
}
