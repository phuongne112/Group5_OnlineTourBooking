package com.example.group5_onlinetourbookingsystem.models;

public class CategoryModel {
    private int id;
    private String name;
    private String image;
    private String description;

    // ✅ Constructor đầy đủ (id, name, image, description)
    public CategoryModel(int id, String name, String image, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    // ✅ Constructor cho trường hợp không có ID (khi thêm danh mục mới)
    public CategoryModel(String name, String image, String description) {
        this.name = name;
        this.image = image;
        this.description = description;
    }

    // ✅ Getter và Setter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
