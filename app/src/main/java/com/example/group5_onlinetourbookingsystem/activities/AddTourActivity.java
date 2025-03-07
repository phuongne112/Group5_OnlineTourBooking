package com.example.group5_onlinetourbookingsystem.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class AddTourActivity extends AppCompatActivity {
    private EditText etTourName, etDestination, etCityId, etPrice, etDuration, etCategoryId, etStartTime, etDescription;
    private Button btnAddTour, btnChooseImage;
    private ImageView ivSelectedImage;
    private String selectedImageName = ""; // Chỉ lưu tên file không có đuôi .jpg, .png
    private MyDatabaseHelper dbHelper;

    private static final int REQUEST_STORAGE_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);

        // Ánh xạ view từ XML
        etTourName = findViewById(R.id.etTourName);
        etDestination = findViewById(R.id.etDestination);
        etCityId = findViewById(R.id.etCityId);
        etPrice = findViewById(R.id.etPrice);
        etDuration = findViewById(R.id.etDuration);
        etCategoryId = findViewById(R.id.etCategoryId);
        etStartTime = findViewById(R.id.etStartTime);
        etDescription = findViewById(R.id.etDescription);
        btnAddTour = findViewById(R.id.btnAddTour);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        dbHelper = new MyDatabaseHelper(this);

        // Xử lý chọn ảnh
        btnChooseImage.setOnClickListener(v -> checkPermissionAndPickImage());

        // Xử lý sự kiện thêm tour
        btnAddTour.setOnClickListener(v -> addTourToDatabase());
    }

    // Kiểm tra và yêu cầu quyền truy cập ảnh
    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_STORAGE_PERMISSION);
            } else {
                pickImageFromGallery();
            }
        } else { // Android 12 trở xuống
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                pickImageFromGallery();
            }
        }
    }

    // Kết quả xin quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Mở thư viện ảnh
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    // Nhận kết quả từ thư viện ảnh
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    selectedImageName = getFileNameWithoutExtension(selectedImageUri); // 🟢 Chỉ lấy tên file không có đuôi .jpg, .png
                    ivSelectedImage.setImageURI(selectedImageUri); // Hiển thị ảnh đã chọn
                }
            }
    );

    // Lấy tên file từ URI và bỏ phần mở rộng (.jpg, .png, ...)
    private String getFileNameWithoutExtension(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                cursor.moveToFirst();
                fileName = cursor.getString(nameIndex);
                cursor.close();
            }
        }
        if (fileName == null) {
            fileName = uri.getPath();
            int cut = fileName.lastIndexOf('/');
            if (cut != -1) {
                fileName = fileName.substring(cut + 1);
            }
        }
        // 🟢 Loại bỏ phần mở rộng (.jpg, .png, .jpeg, ...)
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    // Thêm tour vào database
    private void addTourToDatabase() {
        String name = etTourName.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        int cityId = Integer.parseInt(etCityId.getText().toString().trim());
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        int duration = Integer.parseInt(etDuration.getText().toString().trim());
        int categoryId = Integer.parseInt(etCategoryId.getText().toString().trim());
        String startTime = etStartTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Kiểm tra nếu chưa chọn ảnh
        if (selectedImageName.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🟢 Chỉ lưu tên file ảnh không có phần mở rộng vào database
        dbHelper.addTour(name, destination, cityId, price, duration, selectedImageName, categoryId, startTime, description);

        Toast.makeText(this, "Thêm tour thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
