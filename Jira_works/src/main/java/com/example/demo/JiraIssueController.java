package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/jira")
public class JiraIssueController {

    private final JiraIssueService jiraIssueService;

    @Autowired
    public JiraIssueController(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }
    @GetMapping("/issue1")
    public List<JiraIssue> getAllIssues() {
        return jiraIssueService.findAll();
        }
    
    @GetMapping("/issue")
    public String fetchAndSaveJiraIssue() throws Exception {
        jiraIssueService.fetchAndSaveJiraIssue();
        return "Jira issue fetched and saved successfully!";
    }
}
