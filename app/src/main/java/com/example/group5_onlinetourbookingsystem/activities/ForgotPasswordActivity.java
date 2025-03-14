package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.MainActivity;
import com.example.group5_onlinetourbookingsystem.R;
import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputEditText etEmail;
    private Button btnGetNewPassword, btnBackToLogin;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.editTextText2);
        btnGetNewPassword = findViewById(R.id.button2);
        btnBackToLogin = findViewById(R.id.buttonBackToLogin);
        dbHelper = new MyDatabaseHelper(this);

        btnGetNewPassword.setOnClickListener(v -> resetPassword());

        btnBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        // ✅ Kiểm tra định dạng email
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ Gmail hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Kiểm tra email có tồn tại không
        if (!dbHelper.isUserExists(email)) {
            Toast.makeText(this, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Tạo mật khẩu mới
        String newPassword = generateRandomPassword();

        // ✅ Mã hóa mật khẩu mới
        String hashedPassword = hashPassword(newPassword);

        // ✅ Cập nhật mật khẩu mới vào database
        dbHelper.updatePassword(email, hashedPassword);

        // ✅ Gửi mật khẩu mới qua Email (Chạy trên background thread)
        sendEmail(email, newPassword);
    }


    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
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

    // ✅ Gửi Email dùng JavaMail API
    // ✅ Gửi Email dùng JavaMail API
    private void sendEmail(String recipientEmail, String newPassword) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                final String senderEmail = "travellologr5@gmail.com";  // Thay bằng email thực
                final String senderPassword = "jidi lqhb rgbb louu"; // Thay bằng mật khẩu ứng dụng Gmail

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Tạo phiên SMTP với xác thực
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Reset Password - Online Tour Booking System");
                message.setText("Mật khẩu mới của bạn là: " + newPassword + "\nHãy đăng nhập và đổi mật khẩu ngay!");

                // Gửi email
                Transport.send(message);

                // ✅ Chuyển về Login nếu gửi thành công
                runOnUiThread(() -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Mật khẩu mới đã được gửi!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Đóng ForgotPasswordActivity
                });

            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this,
                        "Lỗi khi gửi email: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    public void gotoLogin(View view) {
        Intent intent = new Intent( this, MainActivity.class);
        startActivity(intent);
    }




}
