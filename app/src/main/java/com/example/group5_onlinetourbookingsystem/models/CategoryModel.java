package com.example.group5_onlinetourbookingsystem.models;

public class CategoryModel {
    private int id;
    private String name;
    private String imagePath; // Thêm biến lưu ảnh

    // Constructor có ảnh
    public CategoryModel(int id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    // Constructor không có ảnh (nếu ảnh không bắt buộc)
    public CategoryModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.imagePath = ""; // Mặc định không có ảnh
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
