package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

public class ContactUsActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextSubject, editTextMessage;
    private Button sendButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // Kiểm tra nếu chưa đăng nhập
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Please login to contact us!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Nếu là admin → chuyển hướng sang trang xem toàn bộ tin nhắn
        int roleId = sessionManager.getUserRoleId(); // ✅ Dùng roleId
        if (roleId == 2) { // ✅ Giả sử role_id = 2 là admin
            startActivity(new Intent(this, AdminViewMessagesActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_contact_us);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSendMessage);

        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();

        editTextName.setText(name);
        editTextEmail.setText(email);
        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        if (subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullMessage = subject + ": " + message;

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        int userId = sessionManager.getUserId();

        long result = dbHelper.addHelpQuestion(userId, fullMessage); // ✅ Gửi vào help_center

        if (result != -1) {
            Toast.makeText(this, "Your question has been sent to the Help Center!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
