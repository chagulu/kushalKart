package com.kushalkart.admin.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String msg = "Duplicate value or constraint violated";
        String specificMsg = ex.getMostSpecificCause().getMessage();
        if (specificMsg.contains("username")) {
            msg = "Username already exists";
        } else if (specificMsg.contains("mobile")) {
            msg = "Mobile number already exists";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", msg);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        ex.printStackTrace(); // optional: log properly in prod
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Something went wrong: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
