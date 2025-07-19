package com.kushalkart.admin.controller;

import com.kushalkart.admin.entity.AdminUser;
import com.kushalkart.admin.entity.Worker;
import com.kushalkart.admin.entity.WorkerKyc;
import com.kushalkart.admin.repository.AdminUserRepository;
import com.kushalkart.admin.repository.WorkerKycRepository;
import com.kushalkart.admin.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/worker/kyc")
public class WorkerKycController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private WorkerKycRepository workerKycRepository;

    /**
     * Submit KYC
     */
    @PostMapping("/{workerId}")
    @Transactional
    public ResponseEntity<?> uploadKyc(
            @PathVariable Long workerId,
            @RequestParam String aadhaarNumber,
            @RequestParam MultipartFile aadhaarFront,
            @RequestParam MultipartFile aadhaarBack,
            @RequestParam MultipartFile workerPhoto,
            @RequestParam Long adminId
    ) throws IOException {

        Worker worker = workerRepository.findById(workerId).orElse(null);
        if (worker == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Worker not found"));
        }

        AdminUser admin = adminUserRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Admin not found"));
        }

        WorkerKyc kyc = new WorkerKyc();
        kyc.setWorker(worker);
        kyc.setAadhaarNumber(maskAadhaar(aadhaarNumber));
        kyc.setAadhaarFrontPath(saveFile(aadhaarFront));
        kyc.setAadhaarBackPath(saveFile(aadhaarBack));
        kyc.setWorkerPhotoPath(saveFile(workerPhoto));
        kyc.setStatus(WorkerKyc.Status.PENDING);
        kyc.setRegisteredBy(admin);

        workerKycRepository.save(kyc);

        return ResponseEntity.ok(new ApiResponse(true, "eKYC submitted successfully", Map.of("kycId", kyc.getId())));
    }

    /**
     * Approve KYC
     */
    @PostMapping("/{kycId}/approve")
    @Transactional
    public ResponseEntity<?> approveKyc(@PathVariable Long kycId) {
        return updateStatus(kycId, WorkerKyc.Status.VERIFIED);
    }

    /**
     * Reject KYC
     */
    @PostMapping("/{kycId}/reject")
    @Transactional
    public ResponseEntity<?> rejectKyc(@PathVariable Long kycId) {
        return updateStatus(kycId, WorkerKyc.Status.REJECTED);
    }

    /**
     * Get KYC Status
     */
    @GetMapping("/{workerId}/status")
    public ResponseEntity<?> getKycStatus(@PathVariable Long workerId) {
        Optional<WorkerKyc> kycOpt = workerKycRepository.findTopByWorkerIdOrderByCreatedAtDesc(workerId);
        if (kycOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "No KYC record found for worker"));
        }

        WorkerKyc kyc = kycOpt.get();

        return ResponseEntity.ok(new ApiResponse(true, "KYC status retrieved successfully", Map.of(
                "kycId", kyc.getId(),
                "status", kyc.getStatus()
        )));
    }

    // Helpers
    private ResponseEntity<?> updateStatus(Long kycId, WorkerKyc.Status status) {
        WorkerKyc kyc = workerKycRepository.findById(kycId).orElse(null);
        if (kyc == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "KYC record not found"));
        }
        kyc.setStatus(status);
        workerKycRepository.save(kyc);
        return ResponseEntity.ok(new ApiResponse(true, "KYC " + status.name()));
    }

    private String maskAadhaar(String aadhaar) {
        if (aadhaar.length() < 4) return "***";
        String last4 = aadhaar.substring(aadhaar.length() - 4);
        return "**** **** **** " + last4;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new java.io.File("/tmp/" + filename));
        return "/tmp/" + filename;
    }

    static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}
