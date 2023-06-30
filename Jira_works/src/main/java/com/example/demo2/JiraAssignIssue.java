package com.example.demo2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JiraAssignIssue {
    private static final String JIRA_BASE_URL = "https://kumargoud.atlassian.net";
    private static final String API_USERNAME = "kumargoud.eedulakanti@ojas-it.com";
    private static final String API_TOKEN = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
    private static final String ISSUE_KEY = "JIW-18"; // Replace with your issue key
    private static final String ASSIGNEE_ACCOUNT_ID = "62d64f5a04a004286c0277d1"; // Replace with the assignee's account ID

    public static void main(String[] args) {
        assignIssue();
    }

    private static void assignIssue() {
        try {
            String apiUrl = buildApiUrl();
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);

            setAuthorizationHeader(connection);
            connection.setRequestProperty("Content-Type", "application/json");

            String requestBody = buildRequestBody();
            connection.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Issue assigned successfully.");
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildApiUrl() {
        return JIRA_BASE_URL + "/rest/api/latest/issue/" + ISSUE_KEY + "/assignee";
    }

    private static void setAuthorizationHeader(HttpURLConnection connection) {
        String auth = API_USERNAME + ":" + API_TOKEN;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;
        connection.setRequestProperty("Authorization", authHeader);
    }

    private static String buildRequestBody() {
        return "{\"accountId\": \"" + ASSIGNEE_ACCOUNT_ID + "\"}";
    }
}
