package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.DrawableImageAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.SimpleTourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;
import java.util.Arrays;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName, etCategoryDescription;
    private ImageView imgCategory;
    private Button btnUpdateCategory, btnDeleteCategory, btnSelectImage;
    private RecyclerView recyclerViewTours;
    private MyDatabaseHelper dbHelper;
    private int categoryId;
    private String imageUri = "";
    private SimpleTourAdapter tourAdapter;
    private ArrayList<TourModel> tourList;

    // Danh sách các ảnh trong drawable (các tên tài nguyên)
    private final String[] drawableImages = {
            "city", "mountain", "sea", "eco", "cultural"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        imgCategory = findViewById(R.id.imgCategory);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        recyclerViewTours = findViewById(R.id.recyclerViewTours);

        dbHelper = new MyDatabaseHelper(this);
        tourList = new ArrayList<>();

        // Khởi tạo tourAdapter
        tourAdapter = new SimpleTourAdapter(this, tourList);
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTours.setAdapter(tourAdapter);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("CATEGORY_ID")) {
            categoryId = intent.getIntExtra("CATEGORY_ID", -1);
            loadCategoryData(categoryId);
            loadToursByCategory(categoryId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID danh mục!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSelectImage.setOnClickListener(v -> showImageSelectionDialog());
        btnUpdateCategory.setOnClickListener(v -> updateCategory());
        btnDeleteCategory.setOnClickListener(v -> confirmDeleteCategory());
    }

    private void showImageSelectionDialog() {
        // Tạo dialog tùy chỉnh
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_image, null);
        builder.setView(dialogView);

        // Thiết lập RecyclerView trong dialog
        RecyclerView recyclerViewImages = dialogView.findViewById(R.id.recyclerViewImages);
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 3)); // Hiển thị dạng lưới 3 cột

        // Tạo adapter cho danh sách ảnh
        DrawableImageAdapter imageAdapter = new DrawableImageAdapter(this, Arrays.asList(drawableImages), imageName -> {
            imageUri = imageName; // Lưu tên tài nguyên ảnh
            int resourceId = getResources().getIdentifier(imageUri, "drawable", getPackageName());
            if (resourceId != 0) {
                imgCategory.setImageResource(resourceId); // Hiển thị ảnh
            } else {
                Toast.makeText(this, "Không tìm thấy ảnh: " + imageUri, Toast.LENGTH_SHORT).show();
            }
            // Đóng dialog sau khi chọn
            AlertDialog dialog = (AlertDialog) recyclerViewImages.getTag();
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        recyclerViewImages.setAdapter(imageAdapter);

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        recyclerViewImages.setTag(dialog); // Lưu dialog để đóng sau khi chọn
        dialog.show();
    }

    private void loadCategoryData(int id) {
        CategoryModel category = dbHelper.getCategoryById(id);
        if (category != null) {
            etCategoryName.setText(category.getName());
            etCategoryDescription.setText(category.getDescription());
            imageUri = category.getImage();
            if (imageUri != null && !imageUri.isEmpty()) {
                int resourceId = getResources().getIdentifier(imageUri, "drawable", getPackageName());
                if (resourceId != 0) {
                    imgCategory.setImageResource(resourceId);
                } else {
                    imgCategory.setImageResource(R.drawable.favorites);
                }
            }
        }
    }

    private void loadToursByCategory(int categoryId) {
        tourList = dbHelper.getToursByCategoryId(categoryId);
        tourAdapter.updateTourList(tourList);
    }

    private void updateCategory() {
        String newName = etCategoryName.getText().toString().trim();
        String newDescription = etCategoryDescription.getText().toString().trim();

        if (newName.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null || imageUri.isEmpty()) {
            CategoryModel category = dbHelper.getCategoryById(categoryId);
            if (category != null) {
                imageUri = category.getImage();
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("image", imageUri);
        values.put("description", newDescription);

        int rowsAffected = db.update("categories", values, "id=?", new String[]{String.valueOf(categoryId)});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteCategory() {
        if (!tourList.isEmpty()) {
            Toast.makeText(this, "Không thể xóa danh mục vì có " + tourList.size() + " tour liên quan!", Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa danh mục này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteCategory())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteCategory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("categories", "id=?", new String[]{String.valueOf(categoryId)});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
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