package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class AddCityActivity extends AppCompatActivity {

    private EditText etCityName;
    private Button btnSaveCity;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        etCityName = findViewById(R.id.etCityName);
        btnSaveCity = findViewById(R.id.btnSaveCity);
        dbHelper = new MyDatabaseHelper(this);

        btnSaveCity.setOnClickListener(v -> addCity());
    }

    private void addCity() {
        String cityName = etCityName.getText().toString().trim();

        if (cityName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên thành phố!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", cityName);

        long result = db.insert("cities", null, values);
        db.close();

        if (result > 0) {
            Toast.makeText(this, "Thêm thành phố thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Báo hiệu rằng dữ liệu đã được thêm thành công
            finish(); // Quay lại màn hình trước đó
        } else {
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
