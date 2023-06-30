package com.example.demo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetProjectdetails {
	//working getting project details based on selection
    public static void main(String[] args) throws ParseException {

        String username = "kumargoud.eedulakanti@ojas-it.com";
        String password = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

        String jsonString = "";
        String url = "https://kumargoud.atlassian.net/rest/api/3/project";

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
                    jsonString += response.append(line);
                }

                reader.close();

                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(jsonString);

                // Print the list of project names
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String name = (String) jsonObject.get("name");
                    System.out.println((i + 1) + ". " + name);
                }

                // Prompt the user to enter the project name
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the project name to fetch related data: ");
                String selectedProjectName = scanner.nextLine();

                // Find the selected project and fetch its related data
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    String name = (String) jsonObject.get("name");
                    if (name.equals(selectedProjectName)) {
                        // Fetch the project details for the selected project
                        String projectId = (String) jsonObject.get("key");
                        String projectUrl = "https://kumargoud.atlassian.net/rest/api/3/project/";
                        projectUrl+=projectId;
                        System.out.println(projectId);
                        System.out.println(projectUrl);
                        // Fetch additional data for the project
                        fetchProjectDetails(projectUrl);
                        
                        break;
                    }
                }
            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void fetchProjectDetails(String projectUrl) {
        try {
            URL apiUrl = new URL(projectUrl);

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the required headers and authentication
            String username = "kumargoud.eedulakanti@ojas-it.com";
            String password = "ATATT3xFfGF0k7GYMeEiRSBwnHSB-N9bIiJTyUTRySNXawXiiTY1OniQ7jXiXf4zEyNiZNAWfBvR2T3j5w9McJmHuaoJ_esP55D29KwLspvAbhImUGI8Bn0WWh4_qZ0_UJhxWH4WSQ-dl9FW8e8Vas6Z8lOWe5rIUzef0cMUkRRzQTGrFNIbBxY=655A6AE0";

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
                    response.append(line);
                }

                reader.close();

                // Parse and process the additional project details
                // ...
                System.out.println("Project details: " + response.toString());
            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
