package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);

        recyclerView = findViewById(R.id.recyclerViewCities);
        btnAddCity = findViewById(R.id.btnAddCity);
        dbHelper = new MyDatabaseHelper(this);

        // Lấy danh sách thành phố từ database
        cityList = dbHelper.getAllCitiesAdmin();

        // Setup RecyclerView
        cityAdapter = new CityAdapter(this, cityList, city -> {
            // Mở màn hình chỉnh sửa thành phố khi bấm vào item
            Intent intent = new Intent(ManageCityActivity.this, EditCityActivity.class);
            intent.putExtra("CITY_ID", city.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cityAdapter);

        // Xử lý nút thêm thành phố
        btnAddCity.setOnClickListener(v -> {
            Intent intent = new Intent(ManageCityActivity.this, AddCityActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách thành phố
        cityList.clear();
        cityList.addAll(dbHelper.getAllCitiesAdmin());
        cityAdapter.notifyDataSetChanged();
    }

}
