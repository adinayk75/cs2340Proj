package com.example.travelapplication.model;

public class TravelPostInfo {
    private String startDate;
    private String endDate;
    private String destination;
    private String accommodation;
    private String diningReservation;
    private String rating;
    private String notes;

    public TravelPostInfo() { }
    public TravelPostInfo(String startDate, String endDate, String destination,
                          String accommodation, String diningReservation, String rating,
                          String notes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodation = accommodation;
        this.diningReservation = diningReservation;
        this.rating = rating;
        this.notes = notes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getDiningReservation() {
        return diningReservation;
    }

    public void setDiningReservation(String diningReservation) {
        this.diningReservation = diningReservation;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
