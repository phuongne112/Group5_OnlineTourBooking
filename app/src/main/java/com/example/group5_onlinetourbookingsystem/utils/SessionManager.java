package com.example.group5_onlinetourbookingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;

import java.util.HashMap;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_PHONE = "userPhone"; // Thêm key số điện thoại

    private static final String KEY_USER_EMAIL = "userEmail"; // Thêm key email

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // 👉 **Lưu thông tin đăng nhập**
    public void createLoginSession(int userId, String userName, String userRole, String email, String phone) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ROLE, userRole);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put("name", pref.getString(KEY_USER_NAME, ""));  // ✅ Dùng đúng key
        user.put("email", pref.getString(KEY_USER_EMAIL, "")); // ✅ Dùng đúng key
        user.put("phone",pref.getString(KEY_USER_PHONE, "")); // 📌 Chưa có số điện thoại, cần lấy từ database nếu có
        return user;
    }



    // 👉 **Kiểm tra trạng thái đăng nhập**
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 👉 **Lấy User ID**
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    // 👉 **Lấy tên User**
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "Guest");
    }

    // 👉 **Lấy vai trò User**
    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "user");
    }

    // 👉 **Lấy Email User**
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }
    public String getUserPhone() {
        return pref.getString(KEY_USER_PHONE, "");
    }

    // 👉 **Xóa session khi đăng xuất**
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
    public void updateUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

}
