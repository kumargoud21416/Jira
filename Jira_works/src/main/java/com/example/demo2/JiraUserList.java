package com.example.demo2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class JiraUserList {
	private static final String JIRA_BASE_URL = "https://kumargoud.atlassian.net";
	private static final String API_USERNAME = "kumargoud.eedulakanti@ojas-it.com";
	private static final String API_TOKEN = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
	// private static final String PROJECT_KEY = "JIW";

	public static void main(String[] args) {
		try {
			String apiUrl = JIRA_BASE_URL + "/rest/api/3/users";
			JSONArray users = getUsers(apiUrl);
			printUserDetails(users);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private static JSONArray getUsers(String apiUrl) throws IOException {
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		setAuthorizationHeader(connection);
		connection.setRequestProperty("Content-Type", "application/json");

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			Reader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
			StringBuilder response = new StringBuilder();
			char[] buffer = new char[4096];
			int charsRead;
			while ((charsRead = inputStreamReader.read(buffer)) != -1) {
				response.append(buffer, 0, charsRead);
			}
			inputStreamReader.close();
			return new JSONArray(response.toString());
		} else {
			throw new IOException("Error: " + responseCode);
		}
	}

	private static void setAuthorizationHeader(HttpURLConnection connection) {
		String auth = API_USERNAME + ":" + API_TOKEN;
		String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + encodedAuth;
		connection.setRequestProperty("Authorization", authHeader);
	}

	private static void printUserDetails(JSONArray users) {
		for (int i = 0; i < users.length(); i++) {
			JSONObject user = users.getJSONObject(i);
			String displayName = user.getString("displayName");
			String accountId = user.getString("accountId");
			// String emailAddress = user.getString("emailAddress");
			System.out.println("User: " + displayName);
			System.out.println("accountId: " + accountId);
		}
	}
}
