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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

import java.util.ArrayList;

public class AddTourActivity extends AppCompatActivity {
    private EditText etTourName, etDestination, etCityId, etPrice, etDuration, etCategoryId, etStartTime, etDescription;
    private Spinner spinnerTourGuides;
    private Button btnAddTour, btnChooseImage;
    private ImageView ivSelectedImage;
    private String selectedImageName = "";
    private MyDatabaseHelper dbHelper;
    private ArrayList<Integer> guideIdList;
    private static final int REQUEST_STORAGE_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        etTourName = findViewById(R.id.etTourName);
        etDestination = findViewById(R.id.etDestination);
        etCityId = findViewById(R.id.etCityId);
        etPrice = findViewById(R.id.etPrice);
        etDuration = findViewById(R.id.etDuration);
        etCategoryId = findViewById(R.id.etCategoryId);
        etStartTime = findViewById(R.id.etStartTime);
        etDescription = findViewById(R.id.etDescription);
        spinnerTourGuides = findViewById(R.id.spinnerTourGuides);
        btnAddTour = findViewById(R.id.btnAddTour);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        dbHelper = new MyDatabaseHelper(this);

        // 🟢 Tải danh sách Tour Guide vào Spinner
        loadTourGuides();

        btnChooseImage.setOnClickListener(v -> checkPermissionAndPickImage());
        btnAddTour.setOnClickListener(v -> addTourToDatabase());
    }

    private void loadTourGuides() {
        ArrayList<String> guideNames = new ArrayList<>();
        guideIdList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllTourGuides(); // Lấy danh sách hướng dẫn viên

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                guideNames.add(name);
                guideIdList.add(id);
            }
            cursor.close();
        }

        if (guideNames.isEmpty()) {
            guideNames.add("Không có hướng dẫn viên");
            guideIdList.add(-1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, guideNames);
        spinnerTourGuides.setAdapter(adapter);
    }

    private void addTourToDatabase() {
        String name = etTourName.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();
        String categoryIdStr = etCategoryId.getText().toString().trim();
        String cityIdStr = etCityId.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || destination.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()
                || categoryIdStr.isEmpty() || cityIdStr.isEmpty() || startTime.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            int cityId = Integer.parseInt(cityIdStr);
            int selectedGuideId = guideIdList.get(spinnerTourGuides.getSelectedItemPosition());

            // 🟢 Thêm tour và lấy ID tour vừa thêm
            int tourId = dbHelper.addTour(name, destination, cityId, price, duration, selectedImageName, categoryId, startTime, description);

            // 🟢 Thêm Guide vào bảng `tour_guides`
            if (selectedGuideId != -1) {
                dbHelper.assignUserToTourGuide(tourId, selectedGuideId);
            }

            Toast.makeText(this, "Thêm tour thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi nhập số: Vui lòng nhập giá trị hợp lệ!", Toast.LENGTH_SHORT).show();
        }
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
                    selectedImageName = getFileNameWithoutExtension(selectedImageUri); // 🟢 Lấy tên file không có đuôi mở rộng
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
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
