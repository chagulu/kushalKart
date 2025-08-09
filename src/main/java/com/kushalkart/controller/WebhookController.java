package com.kushalkart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private BookingController bookingController;

    @PostMapping("/cashgram")
    public ResponseEntity<String> onWebhook(@RequestBody Map<String, Object> payload) {
        // Extract fields from payload
        String event = payload.get("event") != null ? payload.get("event").toString() : null;
        String cashgramId = payload.get("cashgramId") != null ? payload.get("cashgramId").toString() : null;
        String status = payload.get("cashgramStatus") != null ? payload.get("cashgramStatus").toString() : null;

        // Pass to bookingController for processing
        bookingController.handleCashgramWebhook(cashgramId, status);

        return ResponseEntity.ok("Received");
    }
}



