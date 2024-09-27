package com.fancode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Todo {
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getUserId() {
        return userId;
    }

    private int userId;
    private boolean completed;

    // Getters and setters
    public boolean isCompleted() {
        return completed;
    }
}
