package com.example.group5_onlinetourbookingsystem.models;

public class CityModel {
    private int id;
    private String name;

    public CityModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
