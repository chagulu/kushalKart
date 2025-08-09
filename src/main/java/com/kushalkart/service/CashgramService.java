package com.kushalkart.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class CashgramService {

    private String clientId = "CF14189D2BKI1ODRH1C73B2GEEG";
    private String clientSecret = "cfsk_ma_test_e4bcb76ce959e91527a74556ba683dc9_418966bf";
    private String baseUrl = "https://payout-gamma.cashfree.com"; // Sandbox environment base URL

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.localMode:false}")
    private boolean localMode;

    /**
     * Authorizes with Cashfree to get Bearer token.
     *
     * @return Authorization token string
     */
    public String authorize() {
        if (localMode) {
            // Return mock token to bypass real API in local mode
            return "mock-token-for-local";
        }
        try {
            URL url = new URL(baseUrl + "/payout/v1/authorize");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Client-Id", clientId);
            conn.setRequestProperty("X-Client-Secret", clientSecret);
            conn.setDoOutput(true);

            String payload = "{}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            String response = new Scanner(
                    conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8
            ).useDelimiter("\\A").next();

            JsonNode node = mapper.readTree(response);

            String token = node.path("data").path("token").asText(null);
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Authorization failed or token missing. Response: " + response);
            }
            return token;

        } catch (IOException e) {
            throw new RuntimeException("Error authorizing with Cashfree: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a Cashgram payment link.
     */
    public String createCashgram(String cashgramId, BigDecimal amount, String name,
                                 String phone, String email, String expiry, String remarks) {
        if (localMode) {
            // Return a mock URL in local mode
            return "http://localhost/mock-cashgram/" + cashgramId;
        }
        try {
            String token = authorize();
            URL url = new URL(baseUrl + "/payout/v1/createCashgram");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            String payload = String.format(
                    "{\"cashgramId\":\"%s\",\"amount\":%s,\"name\":\"%s\",\"phone\":\"%s\",\"email\":\"%s\",\"linkExpiry\":\"%s\",\"remarks\":\"%s\"}",
                    cashgramId, amount.toPlainString(), name, phone, email, expiry, remarks
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            String response = new Scanner(
                    conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8
            ).useDelimiter("\\A").next();

            JsonNode node = mapper.readTree(response);

            String link = node.path("data").path("link").asText(null);
            if (link == null || link.isEmpty()) {
                throw new RuntimeException("Create Cashgram failed or link missing. Response: " + response);
            }
            return link;

        } catch (IOException e) {
            throw new RuntimeException("Error creating cashgram: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the status of a Cashgram payment.
     */
    public String getCashgramStatus(String cashgramId) {
        if (localMode) {
            // Return a mock status in local mode
            return "PENDING";
        }
        try {
            String token = authorize();
            URL url = new URL(baseUrl + "/payout/v1/getCashgramStatus?cashgramId=" + cashgramId);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            String response = new Scanner(
                    conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8
            ).useDelimiter("\\A").next();

            JsonNode node = mapper.readTree(response);

            String status = node.path("data").path("status").asText(null);
            if (status == null || status.isEmpty()) {
                throw new RuntimeException("Get Cashgram Status failed or status missing. Response: " + response);
            }
            return status;

        } catch (IOException e) {
            throw new RuntimeException("Error getting cashgram status: " + e.getMessage(), e);
        }
    }
}
