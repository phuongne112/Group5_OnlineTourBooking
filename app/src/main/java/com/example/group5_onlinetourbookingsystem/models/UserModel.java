package com.example.group5_onlinetourbookingsystem.models;

public class UserModel {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String birthDate;
    private String image;
    private String roleName;
    private String status; // ✅ Thêm trạng thái tài khoản (active/banned)

    // Constructor mặc định


    // Constructor đầy đủ
    public UserModel(int id, String name, String email, String phone, String birthDate, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.status = status;
    }


    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getStatus() { return status; } // ✅ Getter cho trạng thái
    public void setStatus(String status) { this.status = status; } // ✅ Setter cho trạng thái
}
