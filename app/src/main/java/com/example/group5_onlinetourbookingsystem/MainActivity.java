package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.activities.HomePage;
import com.example.group5_onlinetourbookingsystem.activities.SignUpActivity;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText etEmail, etPassword;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khai báo tham chiếu tới các EditText trên layout (lưu ý: id của ô nhập Email được dùng ở đây là editTextTextUserName)
        etEmail = findViewById(R.id.editTextTextUserName);
        etPassword = findViewById(R.id.editPassword);
        button = findViewById(R.id.button);

        // Khởi tạo đối tượng MyDatabaseHelper
        dbHelper = new MyDatabaseHelper(this);

        // Xử lý sự kiện nhấn nút Login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập Email và Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                int result = dbHelper.checkUserLogin(email, password);
                if(result == 1) {
                    // Đăng nhập thành công
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else if(result == 0) {
                    // Sai mật khẩu
                    Toast.makeText(MainActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                } else if(result == -1) {
                    // Tài khoản không tồn tại
                    Toast.makeText(MainActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức xử lý sự kiện click cho TextView Sign Up (đã liên kết qua thuộc tính android:onClick trong XML)
    public void gotoSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
