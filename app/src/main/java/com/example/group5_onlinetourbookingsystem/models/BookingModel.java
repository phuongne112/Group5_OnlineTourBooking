package com.example.group5_onlinetourbookingsystem.models;

import java.io.Serializable;

public class BookingModel implements Serializable {
    private int id;
    private int userId;
    private int tourId;
    private int adultCount;
    private int childCount;
    private String note;
    private double totalPrice;
    private String status;
    private String paymentStatus;
    private String time;
    private String date;
    private String name;
    private String tourImage;

    // ✅ Thêm thông tin mở rộng
    private String userEmail;
    private String userPhone;
    private String tourName;
    private String tourDesc;

    // ✅ Constructor đơn giản (ví dụ hiển thị tên, thời gian)
    public BookingModel(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public BookingModel() {
    }

    // ✅ Constructor đơn giản để hiển thị tóm tắt booking
    public BookingModel(int id, String date, String status, int adultCount, int childCount, String name) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.name = name;
    }



    // ✅ Constructor đầy đủ thông tin booking
    public BookingModel(int id, int userId, int tourId, int adultCount, int childCount, String note,
                        double totalPrice, String status, String paymentStatus, String time, String date) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.note = note;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.time = time;
        this.date = date;
    }

    // ✅ Constructor để tạo booking mới (không có id)
    public BookingModel(int userId, int tourId, int adultCount, int childCount, String note,
                        double totalPrice, String status, String paymentStatus, String time, String date) {
        this(0, userId, tourId, adultCount, childCount, note, totalPrice, status, paymentStatus, time, date);
    }

    // ✅ Getter
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getTourId() { return tourId; }
    public int getAdultCount() { return adultCount; }
    public int getChildCount() { return childCount; }
    public String getNote() { return note; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getTime() { return time; }
    public String getDate() { return date; }
    public String getName() { return name; }

    public String getUserEmail() { return userEmail; }
    public String getUserPhone() { return userPhone; }
    public String getTourName() { return tourName; }
    public String getTourDesc() { return tourDesc; }
    public String getTourImage() { return tourImage; }

    // ✅ Setter
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTourId(int tourId) { this.tourId = tourId; }
    public void setAdultCount(int adultCount) { this.adultCount = adultCount; }
    public void setChildCount(int childCount) { this.childCount = childCount; }
    public void setNote(String note) { this.note = note; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setTime(String time) { this.time = time; }
    public void setDate(String date) { this.date = date; }
    public void setName(String name) { this.name = name; }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
    public void setTourName(String tourName) { this.tourName = tourName; } // ✅ Đảm bảo setter này được dùng
    public void setTourDesc(String tourDesc) { this.tourDesc = tourDesc; }
    public void setTourImage(String tourImage) { this.tourImage = tourImage; }
}
