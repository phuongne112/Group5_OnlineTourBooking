package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group5_onlinetourbookingsystem.R;

public class UserDetailActivity extends AppCompatActivity {
    private TextView tvUserName, tvEmail, tvPhone, tvBirthDate, tvStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvStatus = findViewById(R.id.tvStatus);

        // Lấy dữ liệu từ Intent
        String name = getIntent().getStringExtra("user_name");
        String email = getIntent().getStringExtra("user_email");
        String phone = getIntent().getStringExtra("user_phone");
        String birthDate = getIntent().getStringExtra("user_birth_date");
        String status = getIntent().getStringExtra("user_status");

        // Hiển thị dữ liệu lên giao diện
        tvUserName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvBirthDate.setText(birthDate);
        tvStatus.setText(status);
    }
}
