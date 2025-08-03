package com.kushalkart.controller;

import com.kushalkart.dto.WorkerListingResponse;
import com.kushalkart.service.WorkerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
public class WorkerApiController {

    private final WorkerService workerService;

    public WorkerApiController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    public Page<WorkerListingResponse> getWorkers(
            @RequestParam Long userId,
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return workerService.getWorkers(userId, serviceId, page, size);
    }
}


