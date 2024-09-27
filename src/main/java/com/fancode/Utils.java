package com.fancode;

import java.util.List;

public class Utils {

    public static boolean isFanCodeUser(User user) {
        double lat = Double.parseDouble(user.getAddress().getGeo().getLat());
        double lon = Double.parseDouble(user.getAddress().getGeo().getLng());
        return lat >= -40 && lat <= 5 && lon >= 5 && lon <= 100;
    }

    public static double calculateCompletionPercentage(List<Todo> todos) {
        int totalTasks = todos.size();
        long completedTasks = todos.stream().filter(Todo::isCompleted).count();
        return totalTasks == 0 ? 0 : (completedTasks * 100.0) / totalTasks;
    }
}
