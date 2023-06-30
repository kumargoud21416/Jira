package com.example.demo;

import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class JiraIssueService {

	private final JiraIssueRepository jiraIssueRepository;

	@Autowired
	public JiraIssueService(JiraIssueRepository jiraIssueRepository) {
		this.jiraIssueRepository = jiraIssueRepository;
	}

	public void fetchAndSaveJiraIssue() throws Exception {
		String url = "https://kumargoud.atlassian.net/rest/api/2/issue/JIW-4";
		String username = "kumargoud.eedulakanti@ojas-it.com";
		String password = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

		try {
			URL apiUrl = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

			String auth = username + ":" + password;
			String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
			String authHeader = "Basic " + encodedAuth;
			connection.setRequestProperty("Authorization", authHeader);

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
				/*
				 * JsonParser parser = new JsonParser(); JsonObject jsonResponse =
				 * parser.parse(response.toString()).getAsJsonObject(); String key =
				 * jsonResponse.get("key").getAsString(); String
				 * summary=jsonResponse.get("summary").getAsString();
				 */
				// Create JiraIssue object and save it to the database
				JiraIssue jiraIssue = new JiraIssue();
				/*
				 * jiraIssue.setKey(key); jiraIssue.setSummary(summary);
				 */// Set the key based on the response
				// Set other properties as per your Jira issue response

				 jiraIssueRepository.save(jiraIssue);
			} else {
				System.out.println("Error: " + responseCode);
			}
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		
		
		}

	public List<JiraIssue> findAll() {
		// TODO Auto-generated method stub
		return jiraIssueRepository.findAll();
	}
	}
