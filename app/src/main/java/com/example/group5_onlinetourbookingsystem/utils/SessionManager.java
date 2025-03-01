package com.example.group5_onlinetourbookingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // 👉 **Lưu thông tin đăng nhập**
    public void createLoginSession(int userId, String userName, String userRole) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);  // 🛠 Lưu userId
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ROLE, userRole);
        editor.apply();
    }

    // 👉 **Kiểm tra trạng thái đăng nhập**
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 👉 **Lấy user ID**
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);  // -1 nếu không có userId
    }

    // 👉 **Lấy tên người dùng**
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "Guest");
    }

    // 👉 **Lấy vai trò người dùng**
    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "user");
    }

    // 👉 **Xóa session khi đăng xuất**
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
