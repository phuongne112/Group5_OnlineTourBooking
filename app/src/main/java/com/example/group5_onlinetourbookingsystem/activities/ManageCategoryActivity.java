package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.AddCategoryActivity;
import com.example.group5_onlinetourbookingsystem.activities.EditCategoryActivity;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;

import java.util.ArrayList;

public class ManageCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryModel> categoryList;
    private MyDatabaseHelper dbHelper;
    private ImageButton btnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back
        }

        recyclerView = findViewById(R.id.recyclerViewCategories);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        dbHelper = new MyDatabaseHelper(this);

        // Khởi tạo danh sách và adapter
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList, category -> {
            Intent intent = new Intent(this, EditCategoryActivity.class);
            intent.putExtra("CATEGORY_ID", category.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(categoryAdapter);

        // Tải dữ liệu lần đầu
        loadCategories();

        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCategoryActivity.class);
            startActivity(intent);
        });
    }

    // Phương thức tải danh sách category từ database
    private void loadCategories() {
        categoryList.clear(); // Xóa dữ liệu cũ
        categoryList.addAll(dbHelper.getAllCategories()); // Thêm dữ liệu mới
        categoryAdapter.notifyDataSetChanged(); // Thông báo adapter cập nhật giao diện
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách category mỗi khi màn hình được hiển thị lại
        loadCategories();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}