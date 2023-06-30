package com.example.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.nio.charset.StandardCharsets;


public class ExampleFetchBulkUserFromJira {
    public static void main(String[] args) {
        final String jiraServerUrl = "https://kumargoud.atlassian.net";
        final String email = "kumargoud.eedulakanti@ojas-it.com";
        final String apiToken = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
        final String accountId = "62d64f5a04a004286c0277d1";
        

        try {
            kong.unirest.HttpResponse<JsonNode> response = Unirest.get(jiraServerUrl + "/rest/api/3/user/bulk")
                    .basicAuth(email, apiToken)
                    .header("Accept", "application/json")
                    .queryString("accountId", accountId)
                    .asJson();

            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
