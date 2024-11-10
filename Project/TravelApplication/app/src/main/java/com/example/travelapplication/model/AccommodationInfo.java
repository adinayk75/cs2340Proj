package com.example.travelapplication.model;

public class AccommodationInfo {

    private String checkInDate;
    private String checkOutDate;
    private String location;
    private int numRooms;
    private String roomType;


    public AccommodationInfo() {
    }

    public AccommodationInfo(String checkInDate, String checkOutDate, String location, int numRooms, String roomType) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.location = location;
        this.numRooms = numRooms;
        this.roomType = roomType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getLocation() {
        return location;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}