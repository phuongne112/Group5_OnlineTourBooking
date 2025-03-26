package com.example.group5_onlinetourbookingsystem.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etPassword, etBirth;
    private Button btnSignUp;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        dbHelper = new MyDatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etBirth = findViewById(R.id.editBirth);
        btnSignUp = findViewById(R.id.btnSignUp);



        // 🔹 Khi nhấn vào etBirth, hiển thị DatePickerDialog
        etBirth.setOnClickListener(v -> showDatePicker());

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String birthDate = etBirth.getText().toString().trim();

            // ✅ Kiểm tra dữ liệu nhập vào
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(SignUpActivity.this, "Email phải là Gmail hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPhone(phone)) {
                Toast.makeText(SignUpActivity.this, "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidBirthDate(birthDate)) {
                Toast.makeText(this, "Ngày sinh không hợp lệ. Người dùng phải đủ 16 tuổi.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Mã hóa mật khẩu trước khi lưu vào database
            String hashedPassword = hashPassword(password);

            long result = dbHelper.addUser(name, email, phone, hashedPassword, birthDate, "",1);

            if (result != -1) {
                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Đăng ký thất bại. Email có thể đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Hàm hiển thị DatePickerDialog khi nhấn vào etBirth
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etBirth.setText(selectedDate); // 🔹 Gán ngày đã chọn vào etBirth
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // ✅ Hàm kiểm tra email có đúng Gmail không
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    // ✅ Hàm kiểm tra số điện thoại hợp lệ (bắt đầu bằng 0, có 10 số)
    private boolean isValidPhone(String phone) {
        return phone.matches("^0[0-9]{9}$");
    }

    // ✅ Hàm kiểm tra mật khẩu hợp lệ (tối thiểu 6 ký tự)
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    // ✅ Hàm mã hóa mật khẩu SHA-256
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
