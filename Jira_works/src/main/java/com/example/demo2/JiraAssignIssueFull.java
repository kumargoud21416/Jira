package com.example.demo2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JiraAssignIssueFull {
	private static final String JIRA_BASE_URL = "https://kumargoud.atlassian.net";
	private static final String API_USERNAME = "kumargoud.eedulakanti@ojas-it.com";
	private static final String API_TOKEN = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

	 public static void main(String[] args) {
	        try {
	            List<Project> projects = getProjects();
	            int selectedProjectIndex = selectProject(projects);
	            if (selectedProjectIndex != -1) {
	                Project selectedProject = projects.get(selectedProjectIndex);
	                List<Issue> issues = getIssues(selectedProject.getKey());
	                int selectedIssueIndex = selectIssue(issues);
	                if (selectedIssueIndex != -1) {
	                    Issue selectedIssue = issues.get(selectedIssueIndex);
	                    List<String> accountIds = getAccountIds(selectedIssue.getKey());
	                    String selectedAccountId = selectAssignee(accountIds);
	                    if (selectedAccountId != null) {
	                        assignIssue(selectedIssue.getKey(), selectedAccountId);
	                        System.out.println("Issue assigned successfully.");
	                    } else {
	                        System.out.println("No valid assignee selected.");
	                    }
	                } else {
	                    System.out.println("No issue selected.");
	                }
	            } else {
	                System.out.println("No project selected.");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	private static List<String> getAccountIds(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<Project> getProjects() throws IOException {
		String apiUrl = JIRA_BASE_URL + "/rest/api/3/project";
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		setAuthorizationHeader(connection);

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			List<Project> projects = new ArrayList<>();
			Scanner scanner = new Scanner(connection.getInputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("\"key\":") && line.contains("\"name\":")) {
					String key = line.split(":")[1].trim().replaceAll("\"", "");
					String name = line.split(":")[3].trim().replaceAll("\"", "");
					projects.add(new Project(key, name));
				}
			}
			return projects;
		} else {
			System.out.println("Error: " + responseCode);
		}
		return Collections.emptyList();
	}

	private static int selectProject(List<Project> projects) {
        System.out.println("Available Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getName() + " (" + projects.get(i).getKey() + ")");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a project (enter number): ");
        int selection = scanner.nextInt();

        if (selection >= 1 && selection <= projects.size()) {
            return selection - 1;
        } else {
            System.out.println("Invalid selection.");
            return -1;
        }
    }

	private static List<Issue> getIssues(String projectKey) throws IOException {
		String apiUrl = JIRA_BASE_URL + "/rest/api/latest/search?jql=project=" + projectKey;
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		connection.setDoOutput(true);
		setAuthorizationHeader(connection);

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			List<Issue> issues = new ArrayList<>();
			Scanner scanner = new Scanner(connection.getInputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("\"key\":")) {
					String key = line.split(":")[1].trim().replaceAll("\"", "");
					String assignee = "";
					if (line.contains("\"assignee\":")) {
						assignee = line.split(":")[1].trim().replaceAll("\"", "");
					}
					issues.add(new Issue(key, assignee));
				}
			}
			return issues;
		} else {
			System.out.println("Error: " + responseCode);
		}
		return Collections.emptyList();
	}

	private static int selectIssue(List<Issue> issues) {
		System.out.println("Available Issues:");
		for (int i = 0; i < issues.size(); i++) {
			System.out.println((i + 1) + ". " + issues.get(i).getKey() + " - Assignee: " + issues.get(i).getAssignee());
		}

		Scanner scanner = new Scanner(System.in);
		System.out.print("Select an issue (enter the number): ");
		int selectedIssueIndex = scanner.nextInt() - 1;
		if (selectedIssueIndex >= 0 && selectedIssueIndex < issues.size()) {
			return selectedIssueIndex;
		} else {
			System.out.println("Invalid issue selection.");
			return -1;
		}
	}

	private static String selectAssignee(List<String> accountIds) {
		System.out.println("Available Account IDs:");
		System.out.println("1. No assignee");
		if (!((Issue) accountIds).getAssignee().isEmpty()) {
			System.out.println("2. " + ((Issue) accountIds).getAssignee());
		}

		Scanner scanner = new Scanner(System.in);
		System.out.print("Select an account ID to assign the issue (enter the number): ");
		int selectedAssigneeIndex = scanner.nextInt();
		if (selectedAssigneeIndex == 1) {
			return null;
		} else if (selectedAssigneeIndex == 2 && !((Issue) accountIds).getAssignee().isEmpty()) {
			return ((Issue) accountIds).getAssignee();
		} else {
			System.out.println("Invalid assignee selection.");
			return null;
		}
	}

	private static String buildRequestBody(String assigneeAccountId) {
		return "{\"accountId\": \"" + assigneeAccountId + "\"}";
	}

	private static void assignIssue(String issueKey, String requestBody) throws IOException {
		String apiUrl = JIRA_BASE_URL + "/rest/api/latest/issue/" + issueKey + "/assignee";
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setDoOutput(true);
		setAuthorizationHeader(connection);
		connection.setRequestProperty("Content-Type", "application/json");

		connection.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.out.println("Error: " + responseCode);
		}
	}

	private static void setAuthorizationHeader(HttpURLConnection connection) {
		String auth = API_USERNAME + ":" + API_TOKEN;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + encodedAuth;
		connection.setRequestProperty("Authorization", authHeader);
	}

	private static class Project {
		private final String key;
		private final String name;

		public Project(String key, String name) {
			this.key = key;
			this.name = name;
		}

		public String getKey() {
			return key;
		}

		public String getName() {
			return name;
		}
	}

	private static class Issue {
		private final String key;
		private final String assignee;

		public Issue(String key, String assignee) {
			this.key = key;
			this.assignee = assignee;
		}

		public String getKey() {
			return key;
		}

		public String getAssignee() {
			return assignee;
		}
	}
}
