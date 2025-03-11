package com.example.group5_onlinetourbookingsystem.activities;

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

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.UserModel;

import java.util.ArrayList;

public class ManageAccountActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private MyDatabaseHelper dbHelper;
    private ArrayList<UserModel> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        tableLayout = findViewById(R.id.tableLayout);
        dbHelper = new MyDatabaseHelper(this);

        loadUserTable();
    }

    private void loadUserTable() {
        userList = dbHelper.getAllUsers();

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
            roleSpinner.setSelection(user.getRoleId() == 1 ? 0 : 1); // ✅ Nếu roleId = 1 là Customer, roleId = 3 là Tour Guide
            row.addView(roleSpinner);

            // 🛠 **Tự động cập nhật Role khi thay đổi**
            roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int newRoleId = (position == 0) ? 1 : 3; // 1: Customer, 3: Tour Guide
                    if (newRoleId != user.getRoleId()) { // 🔥 Chỉ update nếu có thay đổi
                        dbHelper.updateUserRole(user.getId(), newRoleId);
                        user.setRoleId(newRoleId); // Cập nhật model
                        Toast.makeText(ManageAccountActivity.this, "Role updated!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // 👉 **Cột Option (Spinner để chọn Ban/Unban)**
            Spinner statusSpinner = new Spinner(this);
            String[] statuses = {"Active", "Banned"};
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses);
            statusSpinner.setAdapter(statusAdapter);
            statusSpinner.setSelection(user.getStatus().equals("active") ? 0 : 1);
            row.addView(statusSpinner);

            // 🛠 **Tự động cập nhật Status khi thay đổi**
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newStatus = (position == 0) ? "active" : "banned";
                    if (!newStatus.equals(user.getStatus())) { // 🔥 Chỉ update nếu có thay đổi
                        dbHelper.updateUserStatus(user.getId(), newStatus);
                        user.setStatus(newStatus); // Cập nhật model
                        Toast.makeText(ManageAccountActivity.this, "Status updated!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // 👉 **Thêm dòng vào bảng**
            tableLayout.addView(row);
        }
    }
}
