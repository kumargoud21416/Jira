package com.example.demo;

import org.apache.http.HttpResponse;

import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class ExampleFetchTeamMembersFromOrganization {
    public static void main(String[] args) {
    	final String jiraServerUrl = "https://kumargoud.atlassian.net";
        final String email = "kumargoud.eedulakanti@ojas-it.com";
        final String apiToken = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
       // final String accountId = "62d64f5a04a004286c0277d1";
        final String organizationKey = "kumargoud";
        try {
            kong.unirest.HttpResponse<JsonNode> response = Unirest.get(jiraServerUrl + "/rest/servicedeskapi/organization/" + organizationKey + "/user")
                    .basicAuth(email, apiToken)
                    .header("Accept", "application/json")
                    .asJson();
            kong.unirest.json.JSONArray membersArray = response.getBody().getObject().getJSONArray("values");
            System.out.println("Team Members:");
            for (int i = 0; i < membersArray.length(); i++) {
                kong.unirest.json.JSONObject memberObj = membersArray.getJSONObject(i);
                String displayName = memberObj.getString("displayName");
                String emailAddress = memberObj.getString("emailAddress");
                System.out.println("Display Name: " + displayName);
                System.out.println("Email: " + emailAddress);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
