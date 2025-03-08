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
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_PHONE = "userPhone"; // Thêm key số điện thoại
    private static final String KEY_USER_EMAIL = "userEmail"; // Thêm key email
    private static final String KEY_ROLE_ID = "role_id"; // 🛠 Định nghĩa khóa Role ID

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        // Lấy userId từ SessionManager


    }

    // 👉 **Lưu thông tin đăng nhập (LƯU role_id DƯỚI DẠNG int)**

    public void createLoginSession(int userId, String userName, int roleId, String email, String phone) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putInt(KEY_ROLE_ID, roleId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();

        Log.d("SessionManager", "Đã lưu session: userId = " + userId);
    }

    // 👉 **Lấy Role ID (Dưới dạng int, tránh ClassCastException)**
    public int getUserRoleId() {
        return pref.getInt(KEY_ROLE_ID, -1); // ✅ Trả về -1 nếu không tìm thấy
    }

    // 👉 **Lấy toàn bộ thông tin User**
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, ""));
        user.put(KEY_USER_PHONE, pref.getString(KEY_USER_PHONE, ""));
        user.put(KEY_ROLE_ID, String.valueOf(pref.getInt(KEY_ROLE_ID, 1))); // ✅ Trả về role_id dưới dạng String
        return user;
    }

    // 👉 **Kiểm tra trạng thái đăng nhập**
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 👉 **Lấy User ID**
    public int getUserId() {
        int userId = pref.getInt(KEY_USER_ID, -1);
        Log.d("SessionManager", "Lấy userId từ session: " + userId);
        return userId;
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

    // 👉 **Lấy số điện thoại User**
    public String getUserPhone() {
        return pref.getString(KEY_USER_PHONE, "");
    }

    // 👉 **Xóa session khi đăng xuất**
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    // 👉 **Cập nhật số điện thoại**
    public void updateUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

}
