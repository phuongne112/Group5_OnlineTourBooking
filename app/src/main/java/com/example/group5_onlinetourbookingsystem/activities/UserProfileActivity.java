package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.net.Uri;
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

import java.io.File;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView imgUserProfile;
    private TextView tvUserName, tvUserEmail, tvUserPhone, tvUserBirth;
    private Button btnEditProfile, btnLogout;
    private MyDatabaseHelper myDB;
    private SessionManager sessionManager;
    private int userId;
    private String userImagePath = ""; // Đường dẫn ảnh trong CSDL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();
        myDB = new MyDatabaseHelper(this);

        // Kiểm tra nếu chưa đăng nhập
        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không lấy được userId! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
            return;
        }

        // Ánh xạ UI
        imgUserProfile = findViewById(R.id.imgUserProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvUserBirth = findViewById(R.id.tvUserBirth);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Tải thông tin user
        loadUserProfile(userId);

        // Chỉnh sửa hồ sơ
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        });
    }

    private void loadUserProfile(int userId) {
        UserModel user = myDB.getUserById(userId);
        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            tvUserPhone.setText(user.getPhone());
            tvUserBirth.setText(user.getBirthDate());

            // Kiểm tra ảnh từ database
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                userImagePath = user.getImage();

                if (userImagePath.startsWith("content://")) {
                    Glide.with(this)
                            .load(Uri.parse(userImagePath))
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder)
                            .into(imgUserProfile);
                } else {
                    Glide.with(this)
                            .load(Uri.fromFile(new File(userImagePath))) // Chuyển đổi file path thành URI
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder)
                            .into(imgUserProfile);
                }
            } else {
                imgUserProfile.setImageResource(R.drawable.ic_user_placeholder);
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }

}
