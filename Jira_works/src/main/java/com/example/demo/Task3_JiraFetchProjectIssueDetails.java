package com.example.demo;

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

public class Task3_JiraFetchProjectIssueDetails {
	private static final String username = "kumargoud.eedulakanti@ojas-it.com";
	private static final String password = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

	public static void main(String[] args) throws ParseException {
		String url = "https://kumargoud.atlassian.net/rest/api/3/project";
		String jsonString = fetchJsonString(url);

		if (jsonString != null) {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(jsonString);

			// Print the list of project names
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String name = (String) jsonObject.get("name");
				System.out.println((i + 1) + ". " + name);
			}

			// Prompt the user to enter the project name
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter the project name to fetch related data: ");
			String selectedProjectName = scanner.nextLine();

			// Find the selected project and fetch its related data
			for (Object object : jsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				String name = (String) jsonObject.get("name");
				if (name.equals(selectedProjectName)) {
					// Fetch the project details for the selected project
					String projectId = (String) jsonObject.get("key");
					String projectUrl = "https://kumargoud.atlassian.net/rest/api/3/search?jql=project=" + projectId;
					System.out.println(projectId);
					System.out.println(projectUrl);
					// Fetch additional data for the project
					fetchProjectDetails(projectUrl);
					break;
				}
			}
		}
	}

	private static String fetchJsonString(String url) {
		try {
			URL apiUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
			setAuthorizationHeader(connection);

			connection.setRequestMethod("GET");
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
				return response.toString();
			} else {
				System.out.println("Error: " + responseCode);
			}

			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void fetchProjectDetails(String projectUrl) {
		try {
			URL apiUrl = new URL(projectUrl);
			HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
			setAuthorizationHeader(connection);

			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuilder response = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();
				JSONParser parser = new JSONParser();
				JSONObject projectDetails = null;
				JSONObject param2Object = null;
				JSONObject param3Object = null;
				// JSONObject assignee=null;

				// param2Object = (JSONObject) projectDetails.get("issues");
				// param3Object = (JSONObject) param2Object.get("fields");
				try {
					projectDetails = (JSONObject) parser.parse(response.toString());
					JSONArray issuesArray = (JSONArray) projectDetails.get("issues");
					for (Object issue : issuesArray) {
						JSONObject issueObject = (JSONObject) issue;
						JSONObject fieldsObject = (JSONObject) issueObject.get("fields");
						JSONObject creator = (JSONObject) issueObject.get("creator");
						JSONObject assignee = (JSONObject) issueObject.get("assignee");
						String displayName = assignee != null ? (String) assignee.get("displayName") : null;
						String mail = creator != null ? (String) creator.get("emailAddress") : null;

						if (displayName != null) {
							System.out.println("Display Name: " + displayName);
						} else {
							System.out.println("Display Name not found");
						}
						// String displayName = (String) assignee.get("displayName");
						String summary = (String) fieldsObject.get("summary");
						// String mail=(String) creator.get("emailAddress");
						String issueId = (String) issueObject.get("id");
						String issueKey = (String) issueObject.get("key");
						String statusCategoryChangeDate = (String) fieldsObject.get("statuscategorychangedate");
						//System.out.println("Issue creator mail: " + mail);
						if (mail != null) {
							System.out.println("Issue creator mail: " + mail);
						} else {
							System.out.println("Issue creator mail not found");
						}
						System.out.println("Issue assigned to: " + displayName);
						System.out.println("Issue ID: " + issueId);
						System.out.println("Issue Key: " + issueKey);
						System.out.println("Issue summary: " + summary);
						System.out.println("Status Category Change Date: " + statusCategoryChangeDate);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Project details: " + response.toString());
			} else {
				System.out.println("Error: " + responseCode);
			}

			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setAuthorizationHeader(HttpURLConnection connection) {
		String auth = username + ":" + password;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + encodedAuth;
		connection.setRequestProperty("Authorization", authHeader);
	}
}
