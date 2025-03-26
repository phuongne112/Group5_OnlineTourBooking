package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group5_onlinetourbookingsystem.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        // Ánh xạ các mục 01 và 02
        LinearLayout item01 = findViewById(R.id.item_01);
        LinearLayout item02 = findViewById(R.id.item_02);

        // Bắt sự kiện khi nhấn vào 01
        item01.setOnClickListener(v -> openHelpDetail("help_01.txt"));

        // Bắt sự kiện khi nhấn vào 02
        item02.setOnClickListener(v -> openHelpDetail("help_02.txt"));
    }

    private void openHelpDetail(String fileName) {
        Intent intent = new Intent(this, HelpDetailActivity.class);
        intent.putExtra("FILE_NAME", fileName);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
