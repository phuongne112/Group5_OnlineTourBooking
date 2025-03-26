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
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.CityModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddTourActivity extends AppCompatActivity {
    private EditText etTourName, etDestination, etCityId, etPrice, etDuration, etStartTime, etDescription;
    private Spinner spinnerTourGuides, spinnerCategory,spinnerCity;
    private Button btnAddTour, btnChooseImage;
    private ImageView ivSelectedImage;
    private String selectedImageName = "";
    private MyDatabaseHelper dbHelper;
    private ArrayList<Integer> guideIdList, categoryIdList;
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private ArrayList<Integer> cityIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        // Ánh xạ view
        etTourName = findViewById(R.id.etTourName);
        etDestination = findViewById(R.id.etDestination);
        spinnerCity = findViewById(R.id.spinnerCity);
        etPrice = findViewById(R.id.etPrice);
        etDuration = findViewById(R.id.etDuration);
        etStartTime = findViewById(R.id.etStartTime);
        etDescription = findViewById(R.id.etDescription);
        spinnerTourGuides = findViewById(R.id.spinnerTourGuides);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddTour = findViewById(R.id.btnAddTour);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        dbHelper = new MyDatabaseHelper(this);

        // Load dữ liệu vào Spinner
        loadTourGuides();
        loadCategories();
        loadCities();

        btnChooseImage.setOnClickListener(v -> checkPermissionAndPickImage());
        btnAddTour.setOnClickListener(v -> addTourToDatabase());
    }

    private void loadTourGuides() {
        ArrayList<String> guideNames = new ArrayList<>();
        guideIdList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllTourGuides();

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

    private void loadCategories() {
        ArrayList<String> categoryNames = new ArrayList<>();
        categoryIdList = new ArrayList<>();

        // Lấy danh sách danh mục từ cơ sở dữ liệu
        ArrayList<CategoryModel> categoryList = dbHelper.getAllCategories();

        // Duyệt qua danh sách danh mục
        for (CategoryModel category : categoryList) {
            categoryNames.add(category.getName());
            categoryIdList.add(category.getId());
        }

        // Nếu không có danh mục nào, thêm mục mặc định
        if (categoryNames.isEmpty()) {
            categoryNames.add("Không có danh mục");
            categoryIdList.add(-1);
        }

        // Gán danh sách vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        spinnerCategory.setAdapter(adapter);
    }


    private void addTourToDatabase() {
        String name = etTourName.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // ⚠️ XÓA `cityIdStr` khỏi điều kiện kiểm tra
        if (name.isEmpty() || destination.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty() || startTime.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);

            // ✅ Lấy cityId từ spinnerCity thay vì EditText
            int selectedCityId = cityIdList.get(spinnerCity.getSelectedItemPosition());

            int selectedGuideId = (guideIdList.isEmpty()) ? -1 : guideIdList.get(spinnerTourGuides.getSelectedItemPosition());
            int selectedCategoryId = (categoryIdList.isEmpty()) ? -1 : categoryIdList.get(spinnerCategory.getSelectedItemPosition());

            String imageName = (selectedImageName == null) ? "" : selectedImageName;

            int tourId = dbHelper.addTour(name, destination, selectedCityId, price, duration, imageName, selectedCategoryId, startTime, description);

            if (selectedGuideId != -1) {
                dbHelper.assignUserToTourGuide(tourId, selectedGuideId);
            }

            Toast.makeText(this, "Thêm tour thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi nhập số: Vui lòng nhập giá trị hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }


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

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        selectedImageName = copyImageToInternalStorage(selectedImageUri);
                        ivSelectedImage.setImageURI(selectedImageUri);
                    }
                }
            }
    );
    private void loadCities() {
        ArrayList<String> cityNames = new ArrayList<>();
        cityIdList = new ArrayList<>();

        ArrayList<CityModel> cityList = dbHelper.getAllCities2(); // ✅ Sử dụng hàm mới

        for (CityModel city : cityList) {
            cityNames.add(city.getName());
            cityIdList.add(city.getId());
        }

        if (cityNames.isEmpty()) {
            cityNames.add("Không có thành phố");
            cityIdList.add(-1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityNames);
        spinnerCity.setAdapter(adapter);
    }


    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File directory = new File(getFilesDir(), "tour_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
