package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvTotalPrice;
    private Button btnApprove, btnCancel;
    private MyDatabaseHelper dbHelper;
    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTotalPrice = findViewById(R.id.payment_total_price);
        btnApprove = findViewById(R.id.btn_approve_payment);
        btnCancel = findViewById(R.id.btn_cancel_payment);
        dbHelper = new MyDatabaseHelper(this);
        TextView tvTotalPrice = findViewById(R.id.payment_total_price);

        // Nhận tổng tiền từ Intent
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Hiển thị số tiền thanh toán
        tvTotalPrice.setText("Số tiền cần thanh toán: " + String.format("%,.0f", totalPrice) + " VND");


        totalPrice = getIntent().getDoubleExtra("total_price", 0);

        long bookingId = getIntent().getLongExtra("bookingId", -1);

        if (bookingId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy booking", Toast.LENGTH_SHORT).show();
            Log.e("PaymentDebug", "Received invalid Booking ID!");
            finish(); // Đóng activity nếu bookingId không hợp lệ
        } else {
            Log.d("PaymentDebug", "Received Booking ID: " + bookingId);
        }



        btnApprove.setOnClickListener(v -> {
            dbHelper.updateBookingStatus(bookingId, "Confirmed");
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            dbHelper.updateBookingStatus(bookingId, "Canceled");
            Toast.makeText(this, "Thanh toán bị hủy!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
