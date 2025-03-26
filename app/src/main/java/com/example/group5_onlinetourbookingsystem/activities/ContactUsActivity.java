package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

public class ContactUsActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextSubject, editTextMessage;
    private Button sendButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        sessionManager = new SessionManager(this);

        // ✅ Kiểm tra nếu chưa đăng nhập
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Please login to contact us!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSendMessage);

        // Lấy thông tin người dùng từ session
        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();

        // Hiển thị vào ô input
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

        // TODO: Handle sending the message (e.g., send email or save in the database)
        Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
