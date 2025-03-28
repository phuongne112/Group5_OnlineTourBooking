package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {
    private EditText etUserName, etUserEmail, etUserPhone, etNumAdults, etNumChildren, etBookingNote;
    private TextView tvTourName, tvTourPrice, tvTotalPrice, tvTourDuration, tvStartTime,tvTourPrice2;
    private Button btnConfirmBooking;
    private MyDatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private int tourId, userId;
    private double pricePerAdult, pricePerChild, totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        initViews();

        dbHelper = new MyDatabaseHelper(this);
        sessionManager = new SessionManager(this);

        userId = sessionManager.getUserId();
        loadUserData();
        getIncomingIntent();

        // Sự kiện bấm nút xác nhận
        btnConfirmBooking.setOnClickListener(view -> saveBooking());
    }

    private void initViews() {
        tvTourDuration = findViewById(R.id.booking_tour_duration);
        tvStartTime = findViewById(R.id.booking_start_time);
        tvTourName = findViewById(R.id.booking_tour_name);
        tvTourPrice = findViewById(R.id.booking_tour_price);
        tvTourPrice2 = findViewById(R.id.booking_tour_price2);
        tvTotalPrice = findViewById(R.id.total_price);
        etUserName = findViewById(R.id.booking_user_name);
        etUserEmail = findViewById(R.id.booking_user_email);
        etUserPhone = findViewById(R.id.booking_user_phone);
        etNumAdults = findViewById(R.id.num_adults);
        etNumChildren = findViewById(R.id.num_children);
        etBookingNote = findViewById(R.id.booking_note);
        btnConfirmBooking = findViewById(R.id.confirm_booking_button);
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();
        tourId = intent.getIntExtra("tour_id", -1);
        String name = intent.getStringExtra("tour_name");
        pricePerAdult = intent.getDoubleExtra("tour_price", 0);
        pricePerChild = pricePerAdult * 0.5;
        String duration = intent.getStringExtra("tour_duration");
        String startTime = intent.getStringExtra("start_time");



        // Hiển thị dữ liệu lên UI
        if (name != null) {
            tvTourName.setText(name);
        }
        tvTourPrice.setText("Giá: " + pricePerAdult + " $/người lớn");
        tvTourPrice2.setText("Giá: " + pricePerChild + " $/trẻ em");
        tvTourDuration.setText("Thời lượng: " + duration);
        tvStartTime.setText("Start time: " + startTime);

        etNumAdults.addTextChangedListener(new PriceWatcher());
        etNumChildren.addTextChangedListener(new PriceWatcher());
    }

    private void loadUserData() {
        HashMap<String, String> user = sessionManager.getUserDetails();
        etUserName.setText(user.get("name"));
        etUserEmail.setText(user.get("email"));
        etUserPhone.setText(user.get("phone"));
    }

    private void calculateTotalPrice() {
        int adults = parseIntOrZero(etNumAdults.getText().toString());
        int children = parseIntOrZero(etNumChildren.getText().toString());
        totalPrice = (adults * pricePerAdult) + (children * pricePerChild);
        tvTotalPrice.setText("Tổng tiền: " + totalPrice + "$");
    }

    private void saveBooking() {
        if (userId == -1 || tourId == -1) {
            Toast.makeText(this, "Lỗi: Không lấy được thông tin tour hoặc người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        int adults = parseIntOrZero(etNumAdults.getText().toString());
        int children = parseIntOrZero(etNumChildren.getText().toString());

        // Kiểm tra nếu cả vé người lớn và trẻ em đều bằng 0
        if (adults == 0 && children == 0) {
            Toast.makeText(this, "Vui lòng nhập ít nhất một vé người lớn hoặc trẻ em", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = etBookingNote.getText().toString();
        calculateTotalPrice();

        long bookingId = dbHelper.addBooking(userId, tourId, adults, children, note, totalPrice, "Pending");

        if (bookingId == -1) {
            Toast.makeText(this, "Lỗi: Không thể tạo booking", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("BookingDebug", "Booking ID: " + bookingId);

        Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
        intent.putExtra("bookingId", bookingId);
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }

    private int parseIntOrZero(String value) {
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private class PriceWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            calculateTotalPrice();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
