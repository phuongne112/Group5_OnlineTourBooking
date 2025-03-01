package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.UserModel;

public class EditProfileActivity extends AppCompatActivity {
    private EditText edtUserName, edtUserPhone, edtUserBirth;
    private ImageView imgUserProfile;
    private Button btnSaveProfile, btnChangeImage;
    private MyDatabaseHelper myDB;
    private int userId;
    private String selectedImagePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // ➜ Kiểm tra XML có đúng ID chưa
        edtUserName = findViewById(R.id.edtUserName);
        edtUserPhone = findViewById(R.id.edtUserPhone);
        edtUserBirth = findViewById(R.id.edtUserBirth);
        imgUserProfile = findViewById(R.id.imgUserProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);

        myDB = new MyDatabaseHelper(this);
        userId = getIntent().getIntExtra("userId", -1);

        if (userId != -1) {
            loadUserProfile(userId);
        }

        btnChangeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSaveProfile.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserProfile(int userId) {
        UserModel user = myDB.getUserById(userId);
        if (user != null) {
            edtUserName.setText(user.getName());
            edtUserPhone.setText(user.getPhone());
            edtUserBirth.setText(user.getBirthDate());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                imgUserProfile.setImageURI(Uri.parse(user.getImage()));
                selectedImagePath = user.getImage();
            } else {
                imgUserProfile.setImageResource(R.drawable.ic_user_placeholder);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imgUserProfile.setImageURI(imageUri);
            selectedImagePath = imageUri.toString();
        }
    }

    private void updateUserProfile() {
        String newName = edtUserName.getText().toString().trim();
        String newPhone = edtUserPhone.getText().toString().trim();
        String newBirthDate = edtUserBirth.getText().toString().trim();

        if (newName.isEmpty() || newPhone.isEmpty() || newBirthDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = myDB.updateUser(userId, newName, newPhone, newBirthDate, selectedImagePath);

        if (isUpdated) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}
