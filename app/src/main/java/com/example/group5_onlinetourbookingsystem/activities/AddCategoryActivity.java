package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.DrawableImageAdapter;

import java.util.Arrays;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName, etCategoryDescription;
    private ImageView imgCategory;
    private Button btnSaveCategory, btnSelectImage;
    private MyDatabaseHelper dbHelper;
    private String imageUri = ""; // Lưu tên tài nguyên ảnh (ví dụ: "image1")

    // Danh sách các ảnh trong drawable (các tên tài nguyên)
    private final String[] drawableImages = {
            "city", "mountain", "sea", "eco", "cultural" // Thay bằng tên các ảnh trong drawable của bạn
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        imgCategory = findViewById(R.id.imgCategory);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        dbHelper = new MyDatabaseHelper(this);

        btnSelectImage.setOnClickListener(v -> showImageSelectionDialog());
        btnSaveCategory.setOnClickListener(v -> addCategory());
    }

    private void showImageSelectionDialog() {
        // Tạo dialog tùy chỉnh
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_image, null);
        builder.setView(dialogView);

        // Thiết lập RecyclerView trong dialog
        RecyclerView recyclerViewImages = dialogView.findViewById(R.id.recyclerViewImages);
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 3));

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
        recyclerViewImages.setTag(dialog);
        dialog.show();
    }

    private void addCategory() {
        String name = etCategoryName.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn một hình ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", imageUri); // Lưu tên tài nguyên ảnh
        values.put("description", description);

        long result = db.insert("categories", null, values);
        db.close();

        if (result > 0) {
            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Trả về kết quả để cập nhật danh sách
            finish();
        } else {
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}