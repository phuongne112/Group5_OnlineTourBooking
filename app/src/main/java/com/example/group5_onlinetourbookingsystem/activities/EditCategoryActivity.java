package com.example.group5_onlinetourbookingsystem.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;

import java.io.IOException;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName, etCategoryDescription;
    private ImageView imgCategory;
    private Button btnUpdateCategory, btnDeleteCategory, btnSelectImage;
    private MyDatabaseHelper dbHelper;
    private int categoryId;
    private String imageUri = ""; // Lưu đường dẫn ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        imgCategory = findViewById(R.id.imgCategory);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);

        dbHelper = new MyDatabaseHelper(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("CATEGORY_ID")) {
            categoryId = intent.getIntExtra("CATEGORY_ID", -1);
            loadCategoryData(categoryId);
        }

        btnSelectImage.setOnClickListener(v -> openGallery());
        btnUpdateCategory.setOnClickListener(v -> updateCategory());
        btnDeleteCategory.setOnClickListener(v -> confirmDeleteCategory());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    // Lắng nghe kết quả chọn ảnh
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        imageUri = selectedImageUri.toString(); // ✅ Cập nhật đường dẫn ảnh mới
                        imgCategory.setImageURI(Uri.parse(imageUri)); // ✅ Hiển thị ảnh mới
                    }
                }
            });

    private void loadCategoryData(int id) {
        CategoryModel category = dbHelper.getCategoryById(id);
        if (category != null) {
            etCategoryName.setText(category.getName());
            etCategoryDescription.setText(category.getDescription());
            imageUri = category.getImage(); // Lưu đường dẫn ảnh từ database

            // ✅ Hiển thị ảnh cũ nếu có
            if (imageUri != null && !imageUri.isEmpty()) {
                imgCategory.setImageURI(Uri.parse(imageUri));
            }
        }
    }

    private void updateCategory() {
        String newName = etCategoryName.getText().toString().trim();
        String newDescription = etCategoryDescription.getText().toString().trim();

        if (newName.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu không chọn ảnh mới, giữ nguyên ảnh cũ
        if (imageUri == null || imageUri.isEmpty()) {
            CategoryModel category = dbHelper.getCategoryById(categoryId);
            if (category != null) {
                imageUri = category.getImage();
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("image", imageUri); // ✅ Lưu ảnh mới hoặc giữ nguyên ảnh cũ
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
        finish(); // Quay lại màn hình trước đó
        return true;
    }

}
