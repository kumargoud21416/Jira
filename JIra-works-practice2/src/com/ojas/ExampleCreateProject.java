package com.ojas;

import org.apache.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import kong.unirest.Unirest;

public class ExampleCreateProject {

	public static void main(String[] args) {
		// The payload definition using Gson
		JsonObject payload = new JsonObject();
		payload.addProperty("assigneeType", "PROJECT_LEAD");
		payload.addProperty("avatarId", 10200);
		payload.addProperty("categoryId", 10120);
		payload.addProperty("description", "Cloud migration initiative");
		payload.addProperty("issueSecurityScheme", 10001);
		payload.addProperty("key", "EX");
		payload.addProperty("leadAccountId", "62d64f5a04a004286c0277d1");
		payload.addProperty("name", "Example");
		payload.addProperty("notificationScheme", 10021);
		payload.addProperty("permissionScheme", 10011);
		payload.addProperty("projectTemplateKey",
				"com.atlassian.jira-core-project-templates:jira-core-simplified-process-control");
		payload.addProperty("projectTypeKey", "business");
		payload.addProperty("url", "http://atlassian.com");

		// Configure Gson
		Gson gson = new GsonBuilder().create();

		kong.unirest.HttpResponse<kong.unirest.JsonNode> response = Unirest
				.post("https://kumargoud.atlassian.net/rest/api/3/project")
				.basicAuth("kumargoud.eedulakanti@ojas-it.com", "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0").header("Accept", "application/json")
				.header("Content-Type", "application/json").body(gson.toJson(payload)).asJson();

		// Print the response body
		System.out.println(response.getBody());
	}
}
