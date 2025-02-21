package com.example.group5_onlinetourbookingsystem.models;

public class TourModel {
    private int id;
    private String name;
    private String destination;
    private double price;
    private int duration;
    private String image;

    public TourModel(int id, String name, String destination, double price, int duration, String image, String string) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.image = image;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
    public int getDuration() { return duration; }
    public String getImage() { return image; }
}
