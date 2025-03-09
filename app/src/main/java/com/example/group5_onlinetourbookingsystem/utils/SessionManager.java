package com.example.group5_onlinetourbookingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.HashMap;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_ROLE_ID = "role_id"; // 🔹 Role ID dưới dạng int

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // 👉 **Constructor**
    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // 👉 **Lưu thông tin đăng nhập**
    public void createLoginSession(int userId, String userName, int roleId, String email, String phone) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putInt(KEY_ROLE_ID, roleId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply(); // 🔥 Lưu dữ liệu ngay lập tức

        Log.d("SessionManager", "Lưu session thành công: userId=" + userId + ", roleId=" + roleId);
    }

    // 👉 **Lấy Role ID của user (dưới dạng int)**
    public int getUserRoleId() {
        return pref.getInt(KEY_ROLE_ID, -1); // 🔹 Trả về -1 nếu không có dữ liệu
    }

    // 👉 **Lấy toàn bộ thông tin User dưới dạng HashMap**
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, ""));
        user.put(KEY_USER_PHONE, pref.getString(KEY_USER_PHONE, ""));
        user.put(KEY_ROLE_ID, String.valueOf(pref.getInt(KEY_ROLE_ID, -1))); // 🔹 Chuyển role_id thành String
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

    // 👉 **Lấy Email User**
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    // 👉 **Lấy số điện thoại User**
    public String getUserPhone() {
        return pref.getString(KEY_USER_PHONE, "");
    }

    // 👉 **Cập nhật Email**
    public void updateUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    // 👉 **Cập nhật số điện thoại**
    public void updateUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    // 👉 **Cập nhật vai trò của User**
    public void updateUserRole(int roleId) {
        editor.putInt(KEY_ROLE_ID, roleId);
        editor.apply();
    }

    // 👉 **Xóa session khi đăng xuất**
    public void logoutUser() {
        editor.clear(); // 🛑 Chỉ xóa khi user đăng xuất, không xóa khi app restart
        editor.apply();
    }
}
