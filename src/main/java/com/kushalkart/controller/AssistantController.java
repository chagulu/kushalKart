package com.kushalkart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askAssistant(@RequestBody Map<String, String> payload) {
        String userQuery = payload.get("question");
        if (userQuery == null || userQuery.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Question cannot be empty"
            ));
        }

        try {
            String response = callOpenAI(userQuery);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("question", userQuery);
            result.put("answer", response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "Something went wrong: " + e.getMessage()
            ));
        }
    }

    private String callOpenAI(String question) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    { "role": "system", "content": "You are a helpful booking assistant for Kushalkart." },
                    { "role": "user", "content": "%s" }
                ]
            }
        """.formatted(question);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + openaiApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Log response for debugging
        System.out.println("OpenAI Response:\n" + responseBody);

        if (response.statusCode() != 200) {
            throw new RuntimeException("OpenAI API Error: " + responseBody);
        }

        JSONObject json = new JSONObject(responseBody);
        if (!json.has("choices")) {
            throw new RuntimeException("Missing 'choices' field in OpenAI response: " + responseBody);
        }

        return json
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
    }
}
