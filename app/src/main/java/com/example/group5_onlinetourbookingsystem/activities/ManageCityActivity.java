package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.CityAdapter;
import com.example.group5_onlinetourbookingsystem.models.CityModel;

import java.util.ArrayList;

public class ManageCityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private ArrayList<CityModel> cityList;
    private MyDatabaseHelper dbHelper;
    private ImageButton btnAddCity;
    private static final int REQUEST_CODE_EDIT_CITY = 1;
    private static final int REQUEST_CODE_ADD_CITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewCities);
        btnAddCity = findViewById(R.id.btnAddCity);
        dbHelper = new MyDatabaseHelper(this);

        // Tải danh sách thành phố
        loadCityList();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cityAdapter);

        // Xử lý nút thêm thành phố
        btnAddCity.setOnClickListener(v -> {
            Intent intent = new Intent(ManageCityActivity.this, AddCityActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CITY);
        });
    }

    private void loadCityList() {
        // Lấy danh sách thành phố từ database
        cityList = dbHelper.getAllCitiesAdmin();
        if (cityAdapter == null) {
            cityAdapter = new CityAdapter(this, cityList, city -> {
                // Mở màn hình chỉnh sửa thành phố khi bấm vào item
                Intent intent = new Intent(ManageCityActivity.this, EditCityActivity.class);
                intent.putExtra("CITY_ID", city.getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT_CITY);
            });
        } else {
            cityAdapter.updateCityList(cityList); // Cập nhật danh sách trong adapter
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_EDIT_CITY || requestCode == REQUEST_CODE_ADD_CITY) && resultCode == RESULT_OK) {
            // Làm mới danh sách khi chỉnh sửa hoặc thêm thành phố thành công
            loadCityList();
            recyclerView.setAdapter(cityAdapter); // Đảm bảo RecyclerView được cập nhật
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}