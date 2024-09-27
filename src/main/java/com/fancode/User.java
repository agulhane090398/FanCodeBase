package com.fancode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private int id;
    private String name;

    public void setAddress(Address address) {
        this.address = address;
    }

    private Address address;

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
