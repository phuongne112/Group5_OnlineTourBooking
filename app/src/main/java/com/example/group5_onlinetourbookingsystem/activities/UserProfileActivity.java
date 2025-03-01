package com.example.group5_onlinetourbookingsystem.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;

import com.example.group5_onlinetourbookingsystem.MainActivity;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.UserModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvUserPhone, tvUserBirth;
    private ImageView imgUserProfile;
    private Button btnEditProfile, btnLogout;
    private MyDatabaseHelper myDatabaseHelper;
    private SessionManager sessionManager;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        // Ánh xạ giao diện
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvUserBirth = findViewById(R.id.tvUserBirth);
        imgUserProfile = findViewById(R.id.imgUserProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);


        myDatabaseHelper = new MyDatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Lấy userId từ SessionManager
        userId = sessionManager.getUserId();

        // Kiểm tra userId hợp lệ
        if (userId == -1) {
            Toast.makeText(this, "Bạn cần đăng nhập để xem thông tin", Toast.LENGTH_LONG).show();
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        }
        else {
            loadUserProfile(userId);
        }

        // Chỉnh sửa hồ sơ
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, 1);
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserProfile(int userId) {
        UserModel user = myDatabaseHelper.getUserById(userId);
        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            tvUserPhone.setText(user.getPhone());
            tvUserBirth.setText(user.getBirthDate());

            // Kiểm tra ảnh đại diện
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(this).load(user.getImage()).into(imgUserProfile);
            } else {
                imgUserProfile.setImageResource(R.drawable.ic_user_placeholder);

            }
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadUserProfile(userId);
        }
    }
}
