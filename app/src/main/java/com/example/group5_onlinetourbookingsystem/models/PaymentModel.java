package com.example.group5_onlinetourbookingsystem.models;

public class PaymentModel {
    private int id;
    private int bookingId;
    private String status; // "Pending", "Approved", "Canceled"

    public PaymentModel(int id, int bookingId, String status) {
        this.id = id;
        this.bookingId = bookingId;
        this.status = status;
    }

    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
