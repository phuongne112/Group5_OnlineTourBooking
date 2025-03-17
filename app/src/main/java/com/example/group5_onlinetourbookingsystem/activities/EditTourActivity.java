package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;
import java.util.HashMap;

public class EditTourActivity extends AppCompatActivity {

    private EditText etTourName, etDestination, etPrice, etStartTime, etDescription;
    private Spinner spinnerCity, spinnerTourGuides;
    private Button btnSave, btnDeleteTour;
    private MyDatabaseHelper dbHelper;
    private int tourId;
    private HashMap<String, Integer> cityMap;
    private ArrayList<String> cityNames;
    private int selectedCityId, selectedGuideId;
    private ArrayList<Integer> guideIdList, cityIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour);

        etTourName = findViewById(R.id.etTourName);
        etDestination = findViewById(R.id.etDestination);
        etPrice = findViewById(R.id.etPrice);
        etStartTime = findViewById(R.id.etStartTime);
        etDescription = findViewById(R.id.etDescription);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerTourGuides = findViewById(R.id.spinnerTourGuides);
        btnSave = findViewById(R.id.btnSave);
        btnDeleteTour = findViewById(R.id.btnDeleteTour); // ✅ Nút xóa tour

        dbHelper = new MyDatabaseHelper(this);

        // 1️⃣ Load danh sách thành phố TRƯỚC khi load dữ liệu tour
        loadCitySpinner();
        loadTourGuides();

        // 2️⃣ Nhận dữ liệu từ Intent và load tour
        Intent intent = getIntent();
        if (intent.hasExtra("TOUR_ID")) {
            tourId = intent.getIntExtra("TOUR_ID", -1);
            loadTourData(tourId);
        }

        btnSave.setOnClickListener(v -> updateTour());
        btnDeleteTour.setOnClickListener(v -> confirmDeleteTour()); // ✅ Xử lý xóa tour
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
    private void loadTourData(int id) {
        TourModel tour = dbHelper.getTourById(id);
        if (tour != null) {
            etTourName.setText(tour.getName());
            etDestination.setText(tour.getDestination());
            etPrice.setText(String.valueOf(tour.getPrice()));
            etStartTime.setText(tour.getStartTime());
            etDescription.setText(tour.getDescription());

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

            int rowsAffected = db.update("tours", values, "id=?", new String[]{String.valueOf(tourId)});
            db.close();
            // 🟢 Cập nhật Tour Guide trong bảng `tour_guides`
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
}
