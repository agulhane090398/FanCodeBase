package com.fancode;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
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
        // Initialize ExtentSparkReporter (replaces ExtentHtmlReporter)
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("FanCodeTestReport.html");
        sparkReporter.config().setDocumentTitle("FanCode Test Report");
        sparkReporter.config().setReportName("FanCode Automation Report");

        // Initialize ExtentReports and attach the SparkReporter
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        ExtentTest test = extent.createTest("FanCode User Todo Task Completion Test");

        try {
            List<User> users = fetchUsers();
            test.log(Status.INFO, "Fetched users from API.");

            for (User user : users) {
                System.out.println("User id: " + user.getId());
                System.out.println("User name: " + user.getName());
                if (Utils.isFanCodeUser(user)) {
                    List<Todo> todos = fetchTodos(user.getId());
                    double completedPercentage = Utils.calculateCompletionPercentage(todos);

                    if (completedPercentage > 50) {
                        test.log(Status.PASS, "User " + user.getName() + " from FanCode has " + completedPercentage + "% completed tasks.");
                    } else {
                        test.log(Status.FAIL, "User " + user.getName() + " failed the task completion criteria.");
                    }
                } else {
                    test.log(Status.FAIL,"User " + user.getName() + " failed the fancode  user criteria.");
                }
            }

            test.log(Status.INFO, "Test completed.");
        } catch (Exception e) {
            test.log(Status.FAIL, "An error occurred: " + e.getMessage());
        } finally {
            // Finalize and save the report
            extent.flush();
        }
    }

    /**
     * Method will fetch all users from user API
     * @return
     * @throws Exception
     */

    public static List<User> fetchUsers() throws Exception {
        String response = sendGetRequest(USERS_URL);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, new TypeReference<List<User>>() {});
    }

    /**
     * Method will fetch TODO response for specific user
     * @param userId
     * @return
     * @throws Exception
     */
    public static List<Todo> fetchTodos(int userId) throws Exception {
        String response = sendGetRequest(TODOS_URL + userId);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, new TypeReference<List<Todo>>() {});
    }

    /**
     * Method will builc connection and create GET request
     * @param urlString
     * @return
     * @throws Exception
     */
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
