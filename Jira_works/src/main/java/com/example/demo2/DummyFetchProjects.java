package com.example.demo2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;

public class DummyFetchProjects {
    private static final String JIRA_BASE_URL = "https://kumargoud.atlassian.net";
    private static final String API_USERNAME = "kumargoud.eedulakanti@ojas-it.com";
    private static final String API_TOKEN = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

    public static void main(String[] args) {
        try {
            // Fetch the list of projects
            List<Project> projects = getProjects();
            // Select a project
            int projectIndex = selectProject(projects);
            if (projectIndex >= 0 && projectIndex < projects.size()) {
                Project selectedProject = projects.get(projectIndex);
                System.out.println("Selected Project: " + selectedProject.getName());
                String projectKey = selectedProject.getKey();

                // Fetch the list of issues in the selected project
                List<Issue> issues = getIssues(projectKey);
                // Select an issue
                int issueIndex = selectIssue(issues);
                if (issueIndex >= 0 && issueIndex < issues.size()) {
                    Issue selectedIssue = issues.get(issueIndex);
                    System.out.println("Selected Issue: " + selectedIssue.getKey());
                    String issueKey = selectedIssue.getKey();

                    // Fetch the list of account IDs
                    List<String> accountIds = getAccountIds(issueKey);
                    // Select an assignee
                    String assigneeAccountId = selectAssignee(accountIds);
                    if (assigneeAccountId != null) {
                        // Assign the issue to the selected assignee
                        assignIssue(issueKey, assigneeAccountId);
                    } else {
                        System.out.println("No valid assignee selected.");
                    }
                } else {
                    System.out.println("No valid issue selected.");
                }
            } else {
                System.out.println("No valid project selected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Project> getProjects() throws IOException {
        String apiUrl = JIRA_BASE_URL + "/rest/api/latest/project";
        String response = sendGetRequest(apiUrl);
        if (response != null) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = null;
            try {
                jsonArray = (JSONArray) parser.parse(response);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Project> projects = new ArrayList<>();
            if (jsonArray != null) {
                for (Object obj : jsonArray) {
                    JSONObject jsonObj = (JSONObject) obj;
                    String key = (String) jsonObj.get("key");
                    String name = (String) jsonObj.get("name");
                    projects.add(new Project(jsonArray, null, key, null, name, name, null, null, jsonArray, jsonArray, null, jsonArray));
                }
            }
            return projects;
        }
        return Collections.emptyList();
    }

    private static int selectProject(List<Project> projects) {
        System.out.println("Available Projects:");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            System.out.println((i + 1) + ". " + project.getName() + " (" + project.getKey() + ")");
        }
        System.out.print("Select a project by entering its number: ");
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();

        if (selection >= 1 && selection <= projects.size()) {
            return selection - 1;
        } else {
            return -1;
        }
    }

    private static List<Issue> getIssues(String projectKey) throws IOException {
        String apiUrl = JIRA_BASE_URL + "/rest/api/latest/search?jql=project=" + projectKey;
        String response = sendGetRequest(apiUrl);
        if (response != null) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(response);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Issue> issues = new ArrayList<>();
            if (jsonObject != null) {
                JSONArray issueArray = (JSONArray) jsonObject.get("issues");
                for (Object obj : issueArray) {
                    JSONObject issueObj = (JSONObject) obj;
                    String key = (String) issueObj.get("key");
                    issues.add(new Issue(key, null, key, null, null, null, null, key, null, null, issueArray, null, null, null, null, null, issueArray, issueArray, issueArray, null, issueArray, issueArray, null, issueArray, null, issueArray, null, issueArray, issueArray, issueArray, null, null));
                }
            }
            return issues;
        }
        return Collections.emptyList();
    }

    private static int selectIssue(List<Issue> issues) {
        System.out.println("Available Issues:");
        for (int i = 0; i < issues.size(); i++) {
            Issue issue = issues.get(i);
            System.out.println((i + 1) + ". " + issue.getKey());
        }
        System.out.print("Select an issue by entering its number: ");
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();

        if (selection >= 1 && selection <= issues.size()) {
            return selection - 1;
        } else {
            return -1;
        }
    }

    private static List<String> getAccountIds(String issueKey) throws IOException {
        String apiUrl = JIRA_BASE_URL + "/rest/api/latest/issue/" + issueKey + "/assignee";
        String response = sendGetRequest(apiUrl);
        if (response != null) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(response);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<String> accountIds = new ArrayList<>();
            if (jsonObject != null) {
                String accountId = (String) jsonObject.get("accountId");
                if (accountId != null) {
                    accountIds.add(accountId);
                } else {
                    System.out.println("No assignee found for the issue.");
                }
            }
            return accountIds;
        }
        return Collections.emptyList();
    }

    private static String selectAssignee(List<String> accountIds) {
        System.out.println("Available Assignees:");
        for (int i = 0; i < accountIds.size(); i++) {
            String accountId = accountIds.get(i);
            System.out.println((i + 1) + ". " + accountId);
        }
        System.out.print("Select an assignee by entering its number: ");
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();

        if (selection >= 1 && selection <= accountIds.size()) {
            return accountIds.get(selection - 1);
        } else {
            return null;
        }
    }

    private static void assignIssue(String issueKey, String assigneeAccountId) throws IOException {
        String apiUrl = JIRA_BASE_URL + "/rest/api/latest/issue/" + issueKey + "/assignee";
        JSONObject requestBody = new JSONObject();
        requestBody.put("accountId", assigneeAccountId);
        String response = sendPutRequest(apiUrl, requestBody.toJSONString());
        if (response != null) {
            System.out.println("Issue " + issueKey + " assigned to " + assigneeAccountId + " successfully.");
        }
    }

    private static String sendGetRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", getAuthorizationHeader());
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
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
        return null;
    }

    private static String sendPutRequest(String apiUrl, String requestBody) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", getAuthorizationHeader());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
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
        return null;
    }

    private static String getAuthorizationHeader() {
        String auth = API_USERNAME + ":" + API_TOKEN;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encodedAuth;
    }
}
