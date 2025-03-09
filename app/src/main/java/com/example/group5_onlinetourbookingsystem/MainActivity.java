package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.activities.AdminDashboardActivity;
import com.example.group5_onlinetourbookingsystem.activities.ForgotPasswordActivity;
import com.example.group5_onlinetourbookingsystem.activities.HomePage;
import com.example.group5_onlinetourbookingsystem.activities.SignUpActivity;
import com.example.group5_onlinetourbookingsystem.activities.TourGuideDashboardActivity;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button button;
    private MyDatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.editTextTextUserName);
        etPassword = findViewById(R.id.editPassword);
        button = findViewById(R.id.button);

        dbHelper = new MyDatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // 🔹 Thêm tài khoản Admin nếu chưa tồn tại
        addAdminAccount();

        // 🔹 Nếu user đã đăng nhập trước đó, chuyển ngay đến Dashboard tương ứng
        if (sessionManager.isLoggedIn()) {
            navigateToDashboard();
        }

        // Xử lý sự kiện đăng nhập khi bấm nút
        button.setOnClickListener(view -> loginUser());
    }

    // 👉 **Xử lý đăng nhập**
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email phải là Gmail hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = hashPassword(password);
        int result = dbHelper.checkUserLogin(email, hashedPassword);

        if (result == 1) {
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            // 🔹 Lấy thông tin từ database
            int userId = dbHelper.getUserIdByEmail(email);
            String userName = dbHelper.getUserNameByEmail(email);
            String userPhone = dbHelper.getUserPhoneByEmail(email);
            int roleId = dbHelper.getUserRoleIdByEmail(email); // 🔥 Role ID

            // 🔹 Lưu session với đúng role_id
            sessionManager.createLoginSession(userId, userName, roleId, email, userPhone);

            // Chuyển hướng đến đúng màn hình
            navigateToDashboard();
        } else {
            Toast.makeText(this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    // 👉 **Điều hướng đến Dashboard tương ứng**
    private void navigateToDashboard() {
        int roleId = sessionManager.getUserRoleId();
        Log.d("SESSION", "User Role ID: " + roleId);

        Intent intent;
        switch (roleId) {
            case 2: // 🔥 Admin
                intent = new Intent(this, AdminDashboardActivity.class);
                break;
            case 3: // 🔥 Tour Guide
                intent = new Intent(this, TourGuideDashboardActivity.class);
                break;
            default: // 🔥 Customer
                intent = new Intent(this, HomePage.class);
                break;
        }
        startActivity(intent);
        finish(); // Không cho user quay lại màn hình đăng nhập
    }

    // 🛠️ **Hàm thêm tài khoản Admin nếu chưa tồn tại**
    private void addAdminAccount() {
        String adminEmail = "admin@gmail.com";

        if (!dbHelper.isUserExists(adminEmail)) { // Kiểm tra xem Admin đã tồn tại chưa
            String adminName = "Admin";
            String adminPhone = "0123456789";
            String adminPassword = "Admin@123"; // Mật khẩu của Admin
            String adminBirthDate = "1990-01-01"; // Ngày sinh (tùy chọn)
            String adminImagePath = ""; // Nếu có hình ảnh đại diện
            int adminRoleId = 2; // Role ID cho Admin

            // Băm mật khẩu trước khi lưu
            String hashedPassword = hashPassword(adminPassword);

            // Thêm Admin vào database
            long result = dbHelper.addUser(adminName, adminEmail, adminPhone, hashedPassword, adminBirthDate, adminImagePath, adminRoleId);

            if (result != -1) {
                Log.d("ADMIN", "Tạo tài khoản Admin thành công!");
            } else {
                Log.e("ADMIN", "Admin đã tồn tại hoặc có lỗi xảy ra!");
            }
        } else {
            Log.d("ADMIN", "Tài khoản Admin đã tồn tại.");
        }
    }

    // 👉 **Kiểm tra Email có đúng Gmail không**
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    // 👉 **Băm mật khẩu SHA-256**
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 👉 **Chuyển đến màn hình đăng ký**
    public void gotoSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    // 👉 **Chuyển đến màn hình quên mật khẩu**
    public void gotoForgotPassword(View view) {
        Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
