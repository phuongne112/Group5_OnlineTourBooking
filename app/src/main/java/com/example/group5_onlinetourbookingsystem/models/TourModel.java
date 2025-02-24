package com.example.group5_onlinetourbookingsystem.models;

public class TourModel {
    private int id;
    private String name, destination, image, categoryName, cityName;
    private double price;
    private int duration;

    public TourModel(int id, String name, String destination, double price, int duration, String image, String categoryName, String cityName) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.image = image;
        this.categoryName = categoryName;
        this.cityName = cityName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }  // ✅ Thêm getter cho destination
    public double getPrice() { return price; }
    public int getDuration() { return duration; }
    public String getImage() { return image; }
    public String getCategoryName() { return categoryName; }
    public String getCityName() { return cityName; }
}
