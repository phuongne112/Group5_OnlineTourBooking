package com.example.group5_onlinetourbookingsystem.models;

public class RoleModel {
    private int id;
    private String name;

    // Constructor
    public RoleModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
