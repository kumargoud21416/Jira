package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraIssueRepository  extends JpaRepository<JiraIssue, Long> {

}
