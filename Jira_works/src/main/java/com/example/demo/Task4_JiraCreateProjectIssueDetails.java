package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.simple.JSONObject;

public class Task4_JiraCreateProjectIssueDetails {
	private static final String JIRA_BASE_URL = "https://kumargoud.atlassian.net";
	private static final String API_USERNAME = "kumargoud.eedulakanti@ojas-it.com";
	private static final String API_TOKEN = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
	private static final String PROJECT_KEY = "JIW";

	public static void main(String[] args) {
		String issueSummary = "Sample Issue";
		String issueDescription = "................This is a sample issue created via Jira API";

		JSONObject issueObject = new JSONObject();
		issueObject.put("project", new JSONObject().put("key", PROJECT_KEY));
		issueObject.put("summary", issueSummary);
		issueObject.put("description", issueDescription);
		issueObject.put("issuetype", new JSONObject().put("name", "Task"));

		String apiUrl = JIRA_BASE_URL + "/rest/api/3/issue";
		String response = sendPostRequest(apiUrl, issueObject.toJSONString());

		System.out.println("3333333333333333Issue creation response:");
		System.out.println(response);

	}

	private static String sendPostRequest(String apiUrl, String requestBody) {
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			setAuthorizationHeader(connection);
			connection.setRequestProperty("Content-Type", "application/json");

			byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
			connection.getOutputStream().write(requestBodyBytes);

			int responseCode = connection.getResponseCode();
			StringBuilder response = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			reader.close();
			
			
			if (responseCode == HttpURLConnection.HTTP_CREATED) {
				return response.toString();
			} else {
				System.out.println("Error: " + responseCode);
				
				int responseCode1 = connection.getResponseCode();
				System.out.println(responseCode1);
				if (responseCode1 != HttpURLConnection.HTTP_CREATED) {
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
					StringBuilder errorResponse = new StringBuilder();
					String errorLine;
					while ((errorLine = errorReader.readLine()) != null) {
						errorResponse.append(errorLine);
					}
					errorReader.close();
					System.out.println("Error response: " + errorResponse.toString());
				}connection.disconnect();
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void setAuthorizationHeader(HttpURLConnection connection) {
		String auth = API_USERNAME + ":" + API_TOKEN;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + encodedAuth;
		connection.setRequestProperty("Authorization", authHeader);
	}
}
