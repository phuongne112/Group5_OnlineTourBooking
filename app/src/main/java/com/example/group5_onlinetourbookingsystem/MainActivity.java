package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.activities.ForgotPasswordActivity;
import com.example.group5_onlinetourbookingsystem.activities.HomePage;
import com.example.group5_onlinetourbookingsystem.activities.SignUpActivity;
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

        // Nếu user đã đăng nhập trước đó, chuyển ngay đến HomePage
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập Email và Mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(MainActivity.this, "Email phải là Gmail hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = hashPassword(password);
            int result = dbHelper.checkUserLogin(email, hashedPassword);

            if (result == 1) {
                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                // Lưu session đăng nhập
                sessionManager.createLoginSession(email, "Guest"); // Mặc định vai trò là "user"


                // Chuyển đến HomePage
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            } else if (result == 0) {
                Toast.makeText(MainActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

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

    public void gotoSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void gotoForgotPassword(View view) {
        Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
