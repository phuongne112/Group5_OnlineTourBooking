package com.example.group5_onlinetourbookingsystem.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.MainActivity;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.UserModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {
    private EditText edtUserName, edtUserPhone, edtUserBirth;
    private ImageView imgUserProfile;
    private Button btnSaveProfile, btnChangeImage;
    private MyDatabaseHelper myDB;
    private SessionManager sessionManager;
    private int userId;
    private String selectedImagePath = "";

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không lấy được userId! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            finish();
            return;
        }

        edtUserName = findViewById(R.id.edtUserName);
        edtUserPhone = findViewById(R.id.edtUserPhone);
        edtUserBirth = findViewById(R.id.edtUserBirth);
        imgUserProfile = findViewById(R.id.imgUserProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);

        myDB = new MyDatabaseHelper(this);
        loadUserProfile(userId);

        btnChangeImage.setOnClickListener(v -> checkAndRequestPermissions());
        btnSaveProfile.setOnClickListener(v -> updateUserProfile());
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_REQUEST);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                selectedImagePath = getRealPathFromURI(imageUri);
                Glide.with(this).load(imageUri).into(imgUserProfile);
                Log.d("ImagePath", "Selected: " + selectedImagePath);
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.toString();
    }
    private void loadUserProfile(int userId) {
        UserModel user = myDB.getUserById(userId);
        if (user != null) {
            edtUserName.setText(user.getName());
            edtUserPhone.setText(user.getPhone());
            edtUserBirth.setText(user.getBirthDate());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                selectedImagePath = user.getImage();
                Glide.with(this)
                        .load(selectedImagePath.startsWith("content://") ? Uri.parse(selectedImagePath) : Uri.fromFile(new File(selectedImagePath)))
                        .into(imgUserProfile);
            } else {
                imgUserProfile.setImageResource(R.drawable.ic_user_placeholder);
            }
        }
    }

    private boolean isValidBirthDate(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(birthDate);
            return !date.after(new Date());
        } catch (ParseException e) {
            return false;
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

        if (!isValidBirthDate(newBirthDate)) {
            Toast.makeText(this, "Ngày sinh phải đúng định dạng dd/MM/yyyy và không được quá ngày hiện tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ImagePath", "Before check: " + selectedImagePath);
        if (selectedImagePath.isEmpty()) {
            UserModel currentUser = myDB.getUserById(userId);
            if (currentUser != null) {
                selectedImagePath = currentUser.getImage();
            }
        }
        Log.d("ImagePath", "After check: " + selectedImagePath);

        boolean isUpdated = myDB.updateUser(userId, newName, newPhone, newBirthDate, selectedImagePath);

        if (isUpdated) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}
