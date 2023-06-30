package com.example.demo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Extract_data_from_json {
    public static void main(String[] args) {
        String jsonString = "[{\"expand\":\"description,lead,issueTypes,url,projectKeys,permissions,insight\",\"self\":\"https://kumargoud.atlassian.net/rest/api/3/project/10000\",\"id\":\"10000\",\"key\":\"JIW\",\"name\":\"Jira_Integration_works\",\"avatarUrls\":{\"48x48\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10409\",\"24x24\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10409?size=small\",\"16x16\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10409?size=xsmall\",\"32x32\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10409?size=medium\"},\"projectTypeKey\":\"software\",\"simplified\":true,\"style\":\"next-gen\",\"isPrivate\":false,\"properties\":{},\"entityId\":\"197a57f8-22fa-4a27-ba15-6afef230d524\",\"uuid\":\"197a57f8-22fa-4a27-ba15-6afef230d524\"},{\"expand\":\"description,lead,issueTypes,url,projectKeys,permissions,insight\",\"self\":\"https://kumargoud.atlassian.net/rest/api/3/project/10001\",\"id\":\"10001\",\"key\":\"SPT\",\"name\":\"Sample_project_testing\",\"avatarUrls\":{\"48x48\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10403\",\"24x24\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10403?size=small\",\"16x16\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10403?size=xsmall\",\"32x32\":\"https://kumargoud.atlassian.net/rest/api/3/universal_avatar/view/type/project/avatar/10403?size=medium\"},\"projectTypeKey\":\"software\",\"simplified\":true,\"style\":\"next-gen\",\"isPrivate\":false,\"properties\":{},\"entityId\":\"82ed063a-9377-438a-837d-758faf23ab68\",\"uuid\":\"82ed063a-9377-438a-837d-758faf23ab68\"}]";

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(jsonString);

            // Extract name field from each JSON object
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                String name = (String) jsonObject.get("name");
                System.out.println("Name: " + name);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
