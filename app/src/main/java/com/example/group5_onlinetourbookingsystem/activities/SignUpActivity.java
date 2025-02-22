package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etPassword;
    private Button btnSignUp;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        dbHelper = new MyDatabaseHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String image = ""; // Nếu không có hình, để rỗng hoặc set hình mặc định

                // Kiểm tra dữ liệu nhập vào
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thêm người dùng vào cơ sở dữ liệu
                long result = dbHelper.addUser(name, email, phone, password, image);
                if (result != -1) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    // Sau khi đăng ký thành công, chuyển về trang đăng nhập hoặc trang chính
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại. Email có thể đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
