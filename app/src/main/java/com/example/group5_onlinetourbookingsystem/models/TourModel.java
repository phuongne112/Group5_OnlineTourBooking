package com.example.group5_onlinetourbookingsystem.models;

public class TourModel {
    private int id, categoryId;
    private String name, destination, image, cityName, categoryName, description, startTime; // ✅ Thêm startTime
    private double price;
    private int duration;

    public TourModel(int id, String name, String destination, double price, int duration, String image,
                     String description, int categoryId, String categoryName, String cityName, String startTime) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.image = image;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.cityName = cityName;
        this.startTime = startTime;  // ✅ Gán startTime
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
    public int getDuration() { return duration; }
    public String getImage() { return image; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getCityName() { return cityName; }
    public String getStartTime() { return startTime; }  // ✅ Getter cho startTime
}
