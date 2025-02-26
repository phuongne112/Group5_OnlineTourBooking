package com.example.group5_onlinetourbookingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole"; // Nếu bạn cần phân quyền

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // 👉 Lưu thông tin đăng nhập
    public void createLoginSession(String userName, String userRole) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ROLE, userRole); // Lưu vai trò (nếu cần)
        editor.apply();
    }

    // 👉 Kiểm tra trạng thái đăng nhập
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 👉 Lấy tên người dùng đã đăng nhập
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "Guest");
    }

    // 👉 Lấy vai trò người dùng (nếu có)
    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "user"); // Mặc định là "user"
    }

    // 👉 Xóa session khi đăng xuất
    public void logoutUser() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false); // Đánh dấu là chưa đăng nhập
        editor.clear(); // Xóa toàn bộ dữ liệu phiên đăng nhập
        editor.apply();
    }
}
