package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName, etCategoryImage, etCategoryDescription;
    private Button btnSaveCategory;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryImage = findViewById(R.id.etCategoryImage);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        dbHelper = new MyDatabaseHelper(this);

        btnSaveCategory.setOnClickListener(v -> addCategory());
    }

    private void addCategory() {
        String name = etCategoryName.getText().toString().trim();
        String image = etCategoryImage.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("description", description);

        long result = db.insert("categories", null, values);
        db.close();

        if (result > 0) {
            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
