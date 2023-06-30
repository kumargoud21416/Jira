package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "jira_issue")
public class JiraIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`key`", length = 255)
    private String key;

    @Column(name = "summary", length = 255)
    private String summary;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
    // Add other fields as per your Jira issue response

	public JiraIssue(Long id, String key, String summary) {
		super();
		this.id = id;
		this.key = key;
		this.summary = summary;
	}
	public JiraIssue() {
		
		// TODO Auto-generated constructor stub
	}

    // Constructors, getters, and setters
}
