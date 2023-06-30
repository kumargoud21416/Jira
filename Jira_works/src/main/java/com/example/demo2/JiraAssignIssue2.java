package com.example.demo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JiraAssignIssue2 {
	private static final String JIRA_BASE_URL = "https://kumargoud.atlassian.net";
	private static final String API_USERNAME = "kumargoud.eedulakanti@ojas-it.com";
	private static final String API_TOKEN = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

	public static void main(String[] args) {
		try {
			String apiUrl = JIRA_BASE_URL + "/rest/api/3/users";
			JSONArray users = getUsers(apiUrl);
			printUserDetails(users);

			String selectedAccountId = getSelectedAccountId(users);
			String apiUrl = JIRA_BASE_URL + "/rest/api/latest/issue/" + ISSUE_KEY + "/assignee?accountId=" + selectedAccountId;
			sendPutRequest(apiUrl);
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			reader.close();
			connection.disconnect();
			return new JSONArray();
		} else {
			throw new IOException("Error: " + responseCode);
		}
	}

	private static void setAuthorizationHeader(HttpURLConnection connection) {
		String auth = API_USERNAME + ":" + API_TOKEN;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + encodedAuth;
		connection.setRequestProperty("Authorization", authHeader);
	}

	private static void printUserDetails(JSONArray users) {
		for (int i = 0; i < ((CharSequence) users).length(); i++) {
			JSONObject user = users.getJSONObject(i);
			String displayName = user.getString("displayName");
			String accountId = user.getString("accountId");
			System.out.println((i + 1) + ". User: " + displayName);
			System.out.println("   accountId: " + accountId);
		}
	}

	private static String getSelectedAccountId(JSONArray users) {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Enter the number of the user to assign the issue: ");
	    int selectedIndex = scanner.nextInt();
	    scanner.nextLine(); // Consume the newline character

	    JSONObject selectedUser = (JSONObject) users.get(selectedIndex - 1);
	    return selectedUser.getString("accountId");
	}

	private static void sendPutRequest(String apiUrl) throws IOException {
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setDoOutput(true);
		setAuthorizationHeader(connection);
		connection.setRequestProperty("Content-Type", "application/json");

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
			System.out.println("Issue assigned successfully!");
		} else {
			System.out.println("Error: " + responseCode);
		}

		connection.disconnect();
	}
}
