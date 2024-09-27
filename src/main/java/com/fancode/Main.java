package com.fancode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Main {
    private static final String USERS_URL = "http://jsonplaceholder.typicode.com/users";
    private static final String TODOS_URL = "http://jsonplaceholder.typicode.com/todos?userId=";

    public static void main(String[] args) throws Exception {
        List<User> users = fetchUsers();
        for (User user : users) {
            System.out.println("User id: " + user.getId());
            System.out.println("User name: " + user.getName());
            if (Utils.isFanCodeUser(user)) {
                List<Todo> todos = fetchTodos(user.getId());
                double completedPercentage = Utils.calculateCompletionPercentage(todos);

                if (completedPercentage > 50) {
                    System.out.println("User " + user.getName() + " from FanCode has " + completedPercentage + "% completed tasks.");
                } else {
                    System.out.println("User " + user.getName() + " failed the task completion criteria.");
                }
            } else {
                System.out.println("User " + user.getName() + " failed the fancode  user criteria.");
            }
        }
    }

    public static List<User> fetchUsers() throws Exception {
        String response = sendGetRequest(USERS_URL);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, new TypeReference<List<User>>() {});
    }

    public static List<Todo> fetchTodos(int userId) throws Exception {
        String response = sendGetRequest(TODOS_URL + userId);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, new TypeReference<List<Todo>>() {});
    }

    private static String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            output.append(line);
        }

        conn.disconnect();
        return output.toString();
    }
}
