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
    private String time;
    private String date;
    private String name;
    public BookingModel(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }


    public BookingModel(int id, int userId, int tourId, int adultCount, int childCount, String note, double totalPrice, String status, String time, String date) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.note = note;

        this.totalPrice = totalPrice;
        this.status = status;
        this.time = time;
        this.date = date;
    }

    public BookingModel(int userId, int tourId, int adultCount, int childCount, String note, double totalPrice, String status, String time, String date) {
        this.userId = userId;
        this.tourId = tourId;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.note = note;
        this.totalPrice = totalPrice;
        this.status = status;
        this.time = time;
        this.date = date;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getTourId() { return tourId; }
    public int getAdultCount() { return adultCount; }
    public int getChildCount() { return childCount; }
    public String getNote() { return note; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getTime() { return time; }
    public String getDate() { return date; }

    public void setStatus(String status) { this.status = status; }
}
