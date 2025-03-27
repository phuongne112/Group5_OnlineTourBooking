package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    private MyDatabaseHelper dbHelper;
    private SessionManager sessionManager;

    private boolean isOldPasswordVisible = false;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbHelper = new MyDatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // ✅ Kiểm tra trạng thái đăng nhập
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để thay đổi mật khẩu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        ImageView btnToggleOldPassword = findViewById(R.id.btnToggleOldPassword);
        ImageView btnToggleNewPassword = findViewById(R.id.btnToggleNewPassword);
        ImageView btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);

        btnToggleOldPassword.setOnClickListener(v -> togglePasswordVisibility(edtOldPassword, btnToggleOldPassword));
        btnToggleNewPassword.setOnClickListener(v -> togglePasswordVisibility(edtNewPassword, btnToggleNewPassword));
        btnToggleConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(edtConfirmPassword, btnToggleConfirmPassword));
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleButton) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setImageResource(R.drawable.ic_eye_closed);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setImageResource(R.drawable.ic_eye_closed);
        }
        editText.setSelection(editText.getText().length());
    }
    private void changePassword() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(newPassword)) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự và chứa ít nhất 1 chữ in hoa!", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isChanged = dbHelper.changeUserPassword(userId, oldPassword, newPassword);

        if (isChanged) {
            Toast.makeText(this, "Mật khẩu đã được thay đổi thành công!", Toast.LENGTH_SHORT).show();
            Log.d("SUCCESS", "Mật khẩu thay đổi thành công");
            finish();
        } else {
            Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "Mật khẩu cũ không hợp lệ");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Z].*");
    }

}
