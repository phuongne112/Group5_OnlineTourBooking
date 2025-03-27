package com.example.group5_onlinetourbookingsystem.activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAccountActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPhone, etPassword, etBirthDate;
    private RadioGroup rgRole;
    private Button btnSave;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etBirthDate = findViewById(R.id.etBirthDate);
        rgRole = findViewById(R.id.rgRole);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new MyDatabaseHelper(this);

        // Hiển thị DatePickerDialog khi nhấn vào etBirthDate
        etBirthDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> addAccount());
    }

    private void addAccount() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        int selectedRoleId = rgRole.getCheckedRadioButtonId();
        int roleId;

        if (selectedRoleId == R.id.rbUser) {
            roleId = 1;
        } else if (selectedRoleId == R.id.rbTourGuide) {
            roleId = 2;
        } else {
            roleId = 3;
        }

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email phải là Gmail hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhone(phone)) {
            Toast.makeText(this, "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidBirthDate(birthDate)) {
            Toast.makeText(this, "Ngày sinh không hợp lệ. Người dùng phải đủ 16 tuổi.", Toast.LENGTH_SHORT).show();
            return;
        }


        String hashedPassword = hashPassword(password);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", username);
        values.put("email", email);
        values.put("phone", phone);
        values.put("password", hashedPassword);
        values.put("birth_date", birthDate);
        values.put("role_id", roleId);

        long result = db.insert("users", null, values);
        if (result != -1) {
            Toast.makeText(this, "Tài khoản đã được thêm thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm tài khoản thất bại. Email có thể đã tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etBirthDate.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^0[0-9]{9}$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    private boolean isValidBirthDate(String birthDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false);
            Date birth = sdf.parse(birthDate);

            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birth);

            Calendar today = Calendar.getInstance();
            int currentYear = today.get(Calendar.YEAR);
            int birthYear = birthCalendar.get(Calendar.YEAR);

            int age = currentYear - birthYear;

            // Kiểm tra nếu năm sinh vượt quá năm hiện tại hoặc tuổi dưới 16
            return birthYear <= currentYear && age >= 16;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }

}
