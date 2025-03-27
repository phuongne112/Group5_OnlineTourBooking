package com.example.group5_onlinetourbookingsystem.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class EditTourActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private EditText etTourName, etDestination, etPrice, etStartTime, etDescription;
    private ImageView ivTourImage;
    private Button btnSelectImage, btnSave, btnDeleteTour;
    private Spinner spinnerCity, spinnerTourGuides;

    private MyDatabaseHelper dbHelper;
    private int tourId;
    private HashMap<String, Integer> cityMap;
    private ArrayList<String> cityNames;
    private int selectedCityId, selectedGuideId;
    private ArrayList<Integer> guideIdList;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        initializeViews();

        // Load data
        dbHelper = new MyDatabaseHelper(this);
        loadCitySpinner();
        loadTourGuides();

        // Get tour data from intent
        Intent intent = getIntent();
        if (intent.hasExtra("TOUR_ID")) {
            tourId = intent.getIntExtra("TOUR_ID", -1);
            loadTourData(tourId);
        }

        // Set click listeners
        btnSelectImage.setOnClickListener(v -> checkPermissionAndPickImage());
        btnSave.setOnClickListener(v -> updateTour());
        btnDeleteTour.setOnClickListener(v -> confirmDeleteTour());
    }

    private void initializeViews() {
        etTourName = findViewById(R.id.etTourName);
        etDestination = findViewById(R.id.etDestination);
        etPrice = findViewById(R.id.etPrice);
        etStartTime = findViewById(R.id.etStartTime);
        etDescription = findViewById(R.id.etDescription);

        ivTourImage = findViewById(R.id.ivTourImage); // Changed from imgTour
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);
        btnDeleteTour = findViewById(R.id.btnDeleteTour);

        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerTourGuides = findViewById(R.id.spinnerTourGuides);
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
                        selectedImagePath = copyImageToInternalStorage(selectedImageUri);
                        ivTourImage.setImageURI(selectedImageUri);
                    }
                }
            }
    );

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

    private void loadTourData(int id) {
        TourModel tour = dbHelper.getTourById(id);
        if (tour != null) {
            etTourName.setText(tour.getName());
            etDestination.setText(tour.getDestination());
            etPrice.setText(String.valueOf(tour.getPrice()));
            etStartTime.setText(tour.getStartTime());
            etDescription.setText(tour.getDescription());

            // Load and display image
            if (tour.getImage() != null && !tour.getImage().isEmpty()) {
                File imgFile = new File(tour.getImage());
                if (imgFile.exists()) {
                    ivTourImage.setImageURI(Uri.fromFile(imgFile));
                    selectedImagePath = tour.getImage();
                }
            }

            // City spinner selection
            if (cityMap.containsValue(tour.getCityId())) {
                for (String cityName : cityMap.keySet()) {
                    if (cityMap.get(cityName) == tour.getCityId()) {
                        int index = new ArrayList<>(cityMap.keySet()).indexOf(cityName);
                        spinnerCity.setSelection(index);
                        selectedCityId = tour.getCityId();
                        break;
                    }
                }
            }
        }
    }

    private void updateTour() {
        String name = etTourName.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || destination.isEmpty() || priceStr.isEmpty() || startTime.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("destination", destination);
            values.put("price", price);
            values.put("city_id", selectedCityId);
            values.put("start_time", startTime);
            values.put("description", description);

            // Update image path
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                values.put("image", selectedImagePath);
            }

            int rowsAffected = db.update("tours", values, "id=?", new String[]{String.valueOf(tourId)});
            db.close();

            // Update Tour Guide
            dbHelper.updateTourGuide(tourId, selectedGuideId);

            if (rowsAffected > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCitySpinner() {
        cityMap = dbHelper.getAllCitiesWithIds();
        cityNames = new ArrayList<>(cityMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);

        spinnerCity.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCity = (String) parent.getItemAtPosition(position);
                selectedCityId = cityMap.get(selectedCity);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
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

    private void confirmDeleteTour() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa tour này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteTour())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteTour() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("tours", "id=?", new String[]{String.valueOf(tourId)});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Xóa tour thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}