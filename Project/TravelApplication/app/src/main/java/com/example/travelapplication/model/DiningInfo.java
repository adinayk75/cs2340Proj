package com.example.travelapplication.model;

public class DiningInfo {
    private String location;
    private String time;
    private String website;
    private String name;

    // Default constructor (needed for Firebase)
    public DiningInfo() { }

    public DiningInfo(String location, String time, String website, String name) {
        this.location = location;
        this.time = time;
        this.website = website;
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
