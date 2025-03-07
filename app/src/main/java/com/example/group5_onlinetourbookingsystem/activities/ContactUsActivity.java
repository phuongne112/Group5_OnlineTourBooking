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

        sessionManager = new SessionManager(this);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSendMessage);

        // Get user data
        String name = sessionManager.getUserName(); // Example function to get name
        String email = sessionManager.getUserEmail(); // Example function to get email

        // Set user data to EditText fields
        editTextName.setText(name);
        editTextEmail.setText(email);

        // Disable editing for name and email fields
        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);

        // Send message on button click
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
}
