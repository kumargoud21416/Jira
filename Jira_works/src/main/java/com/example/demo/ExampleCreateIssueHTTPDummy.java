package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ExampleCreateIssueHTTPDummy {
    public static void main(String[] args) throws IOException {
        String jiraURL = "https://kumargoud.atlassian.net";
        String username = "kumargoud.eedulakanti@ojas-it.com";
        String password = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
        String projectKey = "JIW";
        String summary = "Example Issue";
        String description = "This is an example issue created via HTTP request.";
        String assigneeEmail = "ravindranath.gundugola@ojas-it.com";

        // Create the issue
        String createIssueEndpoint = jiraURL + "/rest/api/3/issue";
        String issueJson = "{\"fields\":{\"project\":{\"key\":\"" + projectKey + "\"},\"summary\":\"" + summary + "\",\"description\":\"" + description + "\",\"issuetype\":{\"name\":\"Bug\"}}}";
        String issueKey = sendPostRequest(createIssueEndpoint, issueJson, username, password);
        System.out.println("Issue created with key: " + issueKey);

        // Assign the issue to the user
        String assignIssueEndpoint = jiraURL + "/rest/api/3/issue/" + issueKey + "/assignee";
        String assigneeJson = "{\"accountId\":\"" + getAccountIdByEmail(jiraURL, assigneeEmail, username, password) + "\"}";
        sendPutRequest(assignIssueEndpoint, assigneeJson, username, password);
        System.out.println("Issue assigned to: " + assigneeEmail);
    }

    private static String sendPostRequest(String url, String jsonPayload, String username, String password) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL endpoint = new URL(url);
            connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", getAuthorizationHeader(username, password));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonPayload.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                throw new IOException("Request failed with response code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void sendPutRequest(String url, String jsonPayload, String username, String password) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL endpoint = new URL(url);
            connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", getAuthorizationHeader(username, password));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonPayload.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("Request succeeded with response code: " + responseCode);
            } else {
                throw new IOException("Request failed with response code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String getAuthorizationHeader(String username, String password) {
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        return "Basic " + encodedAuth;
    }

    private static String getAccountIdByEmail(String jiraURL, String email, String username, String password) throws IOException {
        String searchUserEndpoint = jiraURL + "/rest/api/3/user/search?query=" + email;
        String response = sendGetRequest(searchUserEndpoint, username, password);
        // Parse the response and extract the user's accountId
        // The response is a JSON array, so you can use a JSON library like Jackson or Gson for parsing
        // Here, we'll use a simple substring search
        int startIndex = response.indexOf("\"accountId\":\"") + "\"accountId\":\"".length();
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex);
    }

    private static String sendGetRequest(String url, String username, String password) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL endpoint = new URL(url);
            connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", getAuthorizationHeader(username, password));

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                throw new IOException("Request failed with response code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
