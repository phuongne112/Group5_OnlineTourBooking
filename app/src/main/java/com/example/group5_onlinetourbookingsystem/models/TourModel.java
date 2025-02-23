package com.example.group5_onlinetourbookingsystem.models;

public class TourModel {
    private int id;
    private String name;
    private String destination;
    private double price;
    private int duration;
    private String image;
    private String categoryName; // Thêm biến này để lưu tên danh mục

    public TourModel(int id, String name, String destination, double price, int duration, String image, String categoryName) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.image = image;
        this.categoryName = categoryName; // Lưu danh mục
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
    public int getDuration() { return duration; }
    public String getImage() { return image; }
    public String getCategoryName() { return categoryName; } // Getter cho categoryName
}
