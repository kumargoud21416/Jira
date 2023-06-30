package com.example.demo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Task0_JiraFetchProjectDetails {
	public static void main(String[] args) throws ParseException {
		
		  String url = "https://kumargoud.atlassian.net/rest/api/3/search"; 
		  String username= "kumargoud.eedulakanti@ojas-it.com";
		  String password = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";
		  String jsonString="";
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
                	jsonString+= response.append(line);
                }

                reader.close();
				/*
				 * JSONParser parser = new JSONParser();
				 * 
				 * JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
				 * 
				 * // Extract name field from each JSON object for (Object object : jsonArray) {
				 * JSONObject jsonObject = (JSONObject) object; String name = (String)
				 * jsonObject.get("name"); System.out.println("Name: " + name);
				 * //System.out.println("iam");
				 * 
				 * }
				 */System.out.println(response.toString());
            }else {
            	System.out.println("iameee");
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
        	System.out.println("iamdd");
            e.printStackTrace();
        }
    }
}

