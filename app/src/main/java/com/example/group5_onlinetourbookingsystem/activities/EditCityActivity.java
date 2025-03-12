package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.CityModel;

public class EditCityActivity extends AppCompatActivity {

    private EditText etCityName;
    private Button btnUpdateCity, btnDeleteCity;
    private MyDatabaseHelper dbHelper;
    private int cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city);

        etCityName = findViewById(R.id.etCityName);
        btnUpdateCity = findViewById(R.id.btnUpdateCity);
        btnDeleteCity = findViewById(R.id.btnDeleteCity); // ✅ Nút xóa thành phố

        dbHelper = new MyDatabaseHelper(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("CITY_ID")) {
            cityId = intent.getIntExtra("CITY_ID", -1);
            loadCityData(cityId);
        }

        btnUpdateCity.setOnClickListener(v -> updateCity());
        btnDeleteCity.setOnClickListener(v -> confirmDeleteCity()); // ✅ Xử lý xóa thành phố
    }

    private void confirmDeleteCity() {
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
            finish();
        } else {
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCityData(int id) {
        CityModel city = dbHelper.getCityById(id);
        if (city != null) {
            etCityName.setText(city.getName());
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
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
