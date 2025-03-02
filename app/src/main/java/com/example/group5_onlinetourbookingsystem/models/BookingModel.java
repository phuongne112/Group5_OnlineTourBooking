package com.example.group5_onlinetourbookingsystem.models;


public class BookingModel {
    private int id;
    private int userId;
    private int tourId;
    private int adultCount;
    private int childCount;
    private String note;
    private double totalPrice;
    private String status; // "Pending", "Confirmed", "Canceled", "Completed"

    public BookingModel(int id, int userId, int tourId, int adultCount, int childCount, String note, double totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.note = note;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    public BookingModel(int userId, int tourId, int adultCount, int childCount, String note, double totalPrice, String status) {
        this.userId = userId;
        this.tourId = tourId;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.note = note;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getTourId() { return tourId; }
    public int getAdultCount() { return adultCount; }
    public int getChildCount() { return childCount; }
    public String getNote() { return note; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}

