package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

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
        tvHeaderName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerRow.addView(tvHeaderName);

        TextView tvHeaderRole = new TextView(this);
        tvHeaderRole.setText("Role");
        tvHeaderRole.setGravity(Gravity.CENTER);
        tvHeaderRole.setTextSize(16);
        tvHeaderRole.setTextColor(Color.BLACK);
        tvHeaderRole.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerRow.addView(tvHeaderRole);

        TextView tvHeaderStatus = new TextView(this);
        tvHeaderStatus.setText("Status");
        tvHeaderStatus.setGravity(Gravity.CENTER);
        tvHeaderStatus.setTextSize(16);
        tvHeaderStatus.setTextColor(Color.BLACK);
        tvHeaderStatus.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
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
            tvName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(tvName);

            // 👉 **Cột Role (TextView thay vì Spinner)**
            TextView tvRole = new TextView(this);
            tvRole.setText(user.getRoleId() == 1 ? "Customer" : "Tour Guide");
            tvRole.setGravity(Gravity.CENTER);
            tvRole.setTextColor(Color.BLACK);
            tvRole.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tvRole.setOnClickListener(v -> {
                // Hiển thị dialog chọn vai trò
                showRoleSelectionDialog(user, tvRole);
            });
            row.addView(tvRole);

            // 👉 **Cột Status (TextView thay vì Spinner)**
            TextView tvStatus = new TextView(this);
            tvStatus.setText(user.getStatus().equals("active") ? "Active" : "Banned");
            tvStatus.setGravity(Gravity.CENTER);
            tvStatus.setTextColor(Color.BLACK);
            tvStatus.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tvStatus.setOnClickListener(v -> {
                // Hiển thị dialog chọn trạng thái
                showStatusSelectionDialog(user, tvStatus);
            });
            row.addView(tvStatus);

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

    // Hàm hiển thị dialog chọn vai trò
    private void showRoleSelectionDialog(UserModel user, TextView tvRole) {
        String[] roles = {"Customer", "Tour Guide"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Role");
        builder.setItems(roles, (dialog, which) -> {
            // Cập nhật vai trò
            int newRoleId = (which == 0) ? 1 : 3; // Customer: 1, Tour Guide: 3
            if (newRoleId != user.getRoleId()) {
                dbHelper.updateUserRole(user.getId(), newRoleId);
                user.setRoleId(newRoleId);
                tvRole.setText(roles[which]); // Cập nhật giao diện
                Toast.makeText(ManageAccountActivity.this, "Role updated!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Hàm hiển thị dialog chọn trạng thái
    private void showStatusSelectionDialog(UserModel user, TextView tvStatus) {
        String[] statuses = {"Active", "Banned"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Status");
        builder.setItems(statuses, (dialog, which) -> {
            // Cập nhật trạng thái
            String newStatus = (which == 0) ? "active" : "banned";
            if (!newStatus.equals(user.getStatus())) {
                dbHelper.updateUserStatus(user.getId(), newStatus);
                user.setStatus(newStatus);
                tvStatus.setText(statuses[which]); // Cập nhật giao diện
                Toast.makeText(ManageAccountActivity.this, "Status updated!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}