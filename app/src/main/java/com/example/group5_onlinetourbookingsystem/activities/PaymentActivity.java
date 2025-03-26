package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvTotalPrice;
    private Button btnApprove, btnCancel;
    private MyDatabaseHelper dbHelper;
    private long bookingId;
    private ImageView imgQrCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        setContentView(R.layout.activity_payment);
        imgQrCode = findViewById(R.id.imgQrCode);
        tvTotalPrice = findViewById(R.id.payment_total_price);
        btnApprove = findViewById(R.id.btn_approve_payment);
        btnCancel = findViewById(R.id.btn_cancel_payment);
        dbHelper = new MyDatabaseHelper(this);
        // Hiển thị ảnh QR mặc định
        imgQrCode.setImageResource(R.drawable.qr_code);
        // Nhận tổng tiền từ Intent
        final double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        bookingId = getIntent().getLongExtra("bookingId", -1);

        // Kiểm tra Booking ID
        if (bookingId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy booking", Toast.LENGTH_SHORT).show();
            Log.e("PaymentDebug", "Received invalid Booking ID!");
            finish(); // Đóng activity nếu bookingId không hợp lệ
        } else {
            Log.d("PaymentDebug", "Received Booking ID: " + bookingId);
        }

        // Hiển thị số tiền
        tvTotalPrice.setText("Số tiền cần thanh toán: " + String.format("%,.0f", totalPrice) + " $");

        // Sự kiện bấm nút "Đã Thanh Toán"
        btnApprove.setOnClickListener(v -> {
            String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

            long paymentId = dbHelper.addPayment(bookingId, totalPrice, currentDate, "Pending");

            if (paymentId != -1) {
                dbHelper.updateBookingStatus(bookingId, "Pending");
                Toast.makeText(this, "Thanh toán thành công! Chờ xác nhận.", Toast.LENGTH_SHORT).show();
                Log.d("PaymentDebug", "Payment ID: " + paymentId);
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi lưu thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện bấm nút "Hủy"
        btnCancel.setOnClickListener(v -> {
            dbHelper.updateBookingStatus(bookingId, "Canceled");
            Toast.makeText(this, "Thanh toán bị hủy!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
