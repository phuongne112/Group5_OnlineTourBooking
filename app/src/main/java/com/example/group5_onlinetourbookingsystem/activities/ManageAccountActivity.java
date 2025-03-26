package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.UserModel;

import java.util.ArrayList;

public class ManageAccountActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private MyDatabaseHelper dbHelper;
    private ArrayList<UserModel> userList;
    private FloatingActionButton btnAddAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        tableLayout = findViewById(R.id.tableLayout);
        btnAddAccount = findViewById(R.id.btnAddAccount);
        dbHelper = new MyDatabaseHelper(this);

        btnAddAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ManageAccountActivity.this, AddAccountActivity.class);
            startActivity(intent);
        });

        loadUserTable();
    }

    // 📌 Cập nhật danh sách khi quay lại trang ManageAccountActivity
    @Override
    protected void onResume() {
        super.onResume();
        reloadUserTable();
    }

    private void reloadUserTable() {
        tableLayout.removeAllViews(); // Xóa toàn bộ dữ liệu cũ
        loadUserTable(); // Tải lại danh sách từ database
    }

    private void loadUserTable() {
        userList = dbHelper.getAllUsers();

        // Xóa hết các dòng cũ trước khi tải lại danh sách
        tableLayout.removeAllViews();

        // 👉 **Thêm hàng tiêu đề**
        TableRow headerRow = new TableRow(this);
        headerRow.setPadding(8, 8, 8, 8);
        headerRow.setBackgroundColor(Color.LTGRAY);

        TextView tvHeaderName = new TextView(this);
        tvHeaderName.setText("Name");
        tvHeaderName.setGravity(Gravity.CENTER);
        tvHeaderName.setTextSize(16);
        tvHeaderName.setTextColor(Color.BLACK);
        headerRow.addView(tvHeaderName);

        TextView tvHeaderRole = new TextView(this);
        tvHeaderRole.setText("Role");
        tvHeaderRole.setGravity(Gravity.CENTER);
        tvHeaderRole.setTextSize(16);
        tvHeaderRole.setTextColor(Color.BLACK);
        headerRow.addView(tvHeaderRole);

        TextView tvHeaderStatus = new TextView(this);
        tvHeaderStatus.setText("Status");
        tvHeaderStatus.setGravity(Gravity.CENTER);
        tvHeaderStatus.setTextSize(16);
        tvHeaderStatus.setTextColor(Color.BLACK);
        headerRow.addView(tvHeaderStatus);

        tableLayout.addView(headerRow);

        // 👉 **Duyệt danh sách user để hiển thị**
        for (UserModel user : userList) {
            TableRow row = new TableRow(this);
            row.setPadding(8, 8, 8, 8);

            // 👉 **Cột Name**
            TextView tvName = new TextView(this);
            tvName.setText(user.getName());
            tvName.setGravity(Gravity.CENTER);
            tvName.setTextColor(Color.BLACK);
            row.addView(tvName);

            // 👉 **Cột Role (Spinner)**
            Spinner roleSpinner = new Spinner(this);
            String[] roles = {"Customer", "Tour Guide"};
            ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
            roleSpinner.setAdapter(roleAdapter);
            roleSpinner.setSelection(user.getRoleId() == 1 ? 0 : 1);
            row.addView(roleSpinner);

            roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int newRoleId = (position == 0) ? 1 : 3;
                    if (newRoleId != user.getRoleId()) {
                        dbHelper.updateUserRole(user.getId(), newRoleId);
                        user.setRoleId(newRoleId);
                        Toast.makeText(ManageAccountActivity.this, "Role updated!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // 👉 **Cột Status (Spinner)**
            Spinner statusSpinner = new Spinner(this);
            String[] statuses = {"Active", "Banned"};
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses);
            statusSpinner.setAdapter(statusAdapter);
            statusSpinner.setSelection(user.getStatus().equals("active") ? 0 : 1);
            row.addView(statusSpinner);

            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newStatus = (position == 0) ? "active" : "banned";
                    if (!newStatus.equals(user.getStatus())) {
                        dbHelper.updateUserStatus(user.getId(), newStatus);
                        user.setStatus(newStatus);
                        Toast.makeText(ManageAccountActivity.this, "Status updated!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // 👉 **Thêm dòng vào bảng**
            tableLayout.addView(row);

            // 👉 **Thêm đường kẻ phân cách**
            View divider = new View(this);
            divider.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    2
            ));
            divider.setBackgroundColor(Color.GRAY);
            tableLayout.addView(divider);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }

}
