package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvTotalPrice;
    private Button btnVNPay, btnCancel;
    private MyDatabaseHelper dbHelper;
    private long bookingId;
    private ImageView imgQrCode;
    private double totalPrice;
    private boolean isPaymentSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        tvTotalPrice = findViewById(R.id.payment_total_price);
        btnVNPay = findViewById(R.id.btn_vnpay_payment);
        btnCancel = findViewById(R.id.btn_cancel_payment);
        dbHelper = new MyDatabaseHelper(this);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        bookingId = getIntent().getLongExtra("bookingId", -1);

        if (bookingId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy booking", Toast.LENGTH_SHORT).show();
            finish();
        }

        tvTotalPrice.setText("Số tiền cần thanh toán: " + String.format("%,.0f", totalPrice) + " VND");

        btnVNPay.setOnClickListener(v -> requestPaymentWithVNPay((int) totalPrice));

        btnCancel.setOnClickListener(v -> {
            cancelBooking();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        cancelBooking();
        return true;
    }

    private void requestPaymentWithVNPay(int amount) {
        String vnp_TmnCode = "YOUR_TMN_CODE";
        String vnp_HashSecret = "YOUR_HASH_SECRET";
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toán đơn hàng " + bookingId;
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";
        String vnp_CurrCode = "VND";
        String vnp_BankCode = "VNPAYQR";
        String vnp_ReturnUrl = "https://yourdomain.com/return";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String vnp_CreateDate = formatter.format(new Date());
        String vnp_ExpireDate = formatter.format(new Date(System.currentTimeMillis() + 15 * 60 * 1000));

        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" +
                "?vnp_Version=" + vnp_Version +
                "&vnp_Command=" + vnp_Command +
                "&vnp_TmnCode=" + vnp_TmnCode +
                "&vnp_Amount=" + (amount * 100) +
                "&vnp_CurrCode=" + vnp_CurrCode +
                "&vnp_TxnRef=" + bookingId +
                "&vnp_OrderInfo=" + vnp_OrderInfo +
                "&vnp_OrderType=" + vnp_OrderType +
                "&vnp_Locale=" + vnp_Locale +
                "&vnp_ReturnUrl=" + vnp_ReturnUrl +
                "&vnp_CreateDate=" + vnp_CreateDate +
                "&vnp_ExpireDate=" + vnp_ExpireDate +
                "&vnp_BankCode=" + vnp_BankCode;

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("paymentUrl", paymentUrl);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && data != null) {
            String status = data.getStringExtra("vnp_ResponseCode");
            if ("00".equals(status)) {
                isPaymentSuccessful = true;
                Toast.makeText(this, "Thanh toán VNPay thành công!", Toast.LENGTH_SHORT).show();
                dbHelper.updateBookingStatus(bookingId, "Completed");
                finish();
            } else {
                Toast.makeText(this, "Thanh toán VNPay thất bại!", Toast.LENGTH_SHORT).show();
                cancelBooking();
            }
        } else {
            cancelBooking();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isPaymentSuccessful) {
            cancelBooking();
        }
    }

    private void cancelBooking() {
        if (bookingId != -1) {
            dbHelper.deleteBooking(bookingId);
            dbHelper.deletePayment(bookingId);
            Toast.makeText(this, "Đã hủy thanh toán, xóa booking và payment!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
