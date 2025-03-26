package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.SimpleTourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CityModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;

public class EditCityActivity extends AppCompatActivity {

    private EditText etCityName;
    private Button btnUpdateCity, btnDeleteCity;
    private RecyclerView recyclerViewTours;
    private MyDatabaseHelper dbHelper;
    private int cityId;
    private SimpleTourAdapter tourAdapter;
    private ArrayList<TourModel> tourList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etCityName = findViewById(R.id.etCityName);
        btnUpdateCity = findViewById(R.id.btnUpdateCity);
        btnDeleteCity = findViewById(R.id.btnDeleteCity);
        recyclerViewTours = findViewById(R.id.recyclerViewTours);

        dbHelper = new MyDatabaseHelper(this);
        tourList = new ArrayList<>();

        // Khởi tạo tourAdapter trước
        tourAdapter = new SimpleTourAdapter(this, tourList);
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTours.setAdapter(tourAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra("CITY_ID")) {
            cityId = intent.getIntExtra("CITY_ID", -1);
            loadCityData(cityId);
            loadToursByCity(cityId); // Gọi sau khi tourAdapter đã được khởi tạo
        } else {
            Toast.makeText(this, "Không tìm thấy ID thành phố!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdateCity.setOnClickListener(v -> updateCity());
        btnDeleteCity.setOnClickListener(v -> confirmDeleteCity());
    }

    private void loadToursByCity(int cityId) {
        tourList = dbHelper.getToursByCityId(cityId);
        tourAdapter.updateTourList(tourList); // Bây giờ tourAdapter đã được khởi tạo
    }

    private void confirmDeleteCity() {
        if (!tourList.isEmpty()) {
            Toast.makeText(this, "Không thể xóa thành phố vì có " + tourList.size() + " tour liên quan!", Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa thành phố này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteCity())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteCity() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("cities", "id=?", new String[]{String.valueOf(cityId)});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Xóa thành phố thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCityData(int id) {
        CityModel city = dbHelper.getCityById(id);
        if (city != null) {
            etCityName.setText(city.getName());
        } else {
            Toast.makeText(this, "Không tìm thấy thành phố!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateCity() {
        String newName = etCityName.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên thành phố!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);

        int rowsAffected = db.update("cities", values, "id=?", new String[]{String.valueOf(cityId)});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}