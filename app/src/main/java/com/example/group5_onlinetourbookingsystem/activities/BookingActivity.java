package com.example.group5_onlinetourbookingsystem.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    private TextView tourName, tourPrice;
    private EditText numAdults, numChildren, numInfants;
    private LinearLayout passengerContainer;
    private Button confirmBooking;
    private MyDatabaseHelper dbHelper;
    private int tourId, userId = 1; // Thay bằng user đã đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Ánh xạ View
        tourName = findViewById(R.id.booking_tour_name);
        tourPrice = findViewById(R.id.booking_tour_price);
        numAdults = findViewById(R.id.num_adults);
        numChildren = findViewById(R.id.num_children);
        numInfants = findViewById(R.id.num_infants);
        passengerContainer = findViewById(R.id.passenger_container);
        confirmBooking = findViewById(R.id.confirm_booking_button);
        dbHelper = new MyDatabaseHelper(this);

        // Lấy dữ liệu từ Intent
        tourId = getIntent().getIntExtra("tour_id", -1);
        tourName.setText(getIntent().getStringExtra("tour_name"));
        tourPrice.setText(getIntent().getStringExtra("tour_price"));

        // Xử lý sự kiện nhấn nút
        confirmBooking.setOnClickListener(v -> {
            if (validateInputs()) {
                bookTour();
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ số lượng hành khách!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        return !TextUtils.isEmpty(numAdults.getText().toString().trim()) &&
                !TextUtils.isEmpty(numChildren.getText().toString().trim()) &&
                !TextUtils.isEmpty(numInfants.getText().toString().trim());
    }

    private void bookTour() {
        int adults = Integer.parseInt(numAdults.getText().toString());
        int children = Integer.parseInt(numChildren.getText().toString());
        int infants = Integer.parseInt(numInfants.getText().toString());
        double totalPrice = calculateTotalPrice(adults, children, infants);

        boolean success = dbHelper.insertBooking(userId, tourId, totalPrice, adults, children, infants);

        if (success) {
            Toast.makeText(this, "Đặt tour thành công!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi đặt tour!", Toast.LENGTH_LONG).show();
        }
    }

    private double calculateTotalPrice(int adults, int children, int infants) {
        double pricePerPerson = Double.parseDouble(tourPrice.getText().toString().replace("$", ""));
        return (adults + children + infants) * pricePerPerson;
    }
}
