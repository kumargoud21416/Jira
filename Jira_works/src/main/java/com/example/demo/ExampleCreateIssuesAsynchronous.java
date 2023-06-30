package com.example.demo;

import java.net.URI;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

public class ExampleCreateIssuesAsynchronous {
	
	//working code only creates issue not assignee

    public Issue createIssue(String project, Long key, String summary, String description, String assignee) throws Exception {
        final URI jiraServerUri = new URI("https://kumargoud.atlassian.net");

        System.out.println("\nConnecting to create a bug...");

        JiraRestClientFactory restClientFactory = new AsynchronousJiraRestClientFactory();
        JiraRestClient restClient = restClientFactory.createWithBasicHttpAuthentication(jiraServerUri, "kumargoud.eedulakanti@ojas-it.com", "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0");
        IssueRestClient issueClient = restClient.getIssueClient();

        Iterable<IssueType> issueTypes = restClient.getMetadataClient().getIssueTypes().claim();

        System.out.println("Available Issue Types:");
        for (IssueType issueType : issueTypes) {
            System.out.println("Issue Type: " + issueType.getName() + ", Key: " + issueType.getId() + ", Desc: " + issueType.getDescription());
        }

        try {
            IssueInputBuilder issueBuilder = new IssueInputBuilder(project, key, summary);
            issueBuilder.setDescription(description);

            IssueType it = new IssueType(jiraServerUri, key, summary, false, "Testing the Issue creation", null);

            issueBuilder.setIssueType(it);
            issueBuilder.setAssigneeName("assignee"); // Set the assignee

            IssueInput issueInput = issueBuilder.build();

            Promise<BasicIssue> promise = restClient.getIssueClient().createIssue(issueInput);
            BasicIssue basicIssue = promise.claim();
            Promise<Issue> promiseJavaIssue = restClient.getIssueClient().getIssue(basicIssue.getKey());

            Issue issue = promiseJavaIssue.claim();
            System.out.println(String.format("New issue created: %s", issue.getSummary()));

            return issue;
        } catch (Exception e) {
            System.out.println("Failed to create issue: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        ExampleCreateIssuesAsynchronous a = new ExampleCreateIssuesAsynchronous();
        a.createIssue("JIW", (long) 10001, "555551Ravindra Create one issue in Jira", "Using a Java program to create the issue in JIW122", "ravindranath.gundugola@ojas-it.com");
    }
}
