package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.GuideBookingAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.ArrayList;

public class GuideListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBookings;
    private GuideBookingAdapter bookingAdapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<BookingModel> bookingList;
    private int tourId, guideId;
    private TextView noBookingsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        noBookingsText = findViewById(R.id.noBookingsText);

        dbHelper = new MyDatabaseHelper(this);

        tourId = getIntent().getIntExtra("tour_id", -1);
        guideId = getIntent().getIntExtra("guide_id", -1);

        Log.d("GuideListActivity", "📌 Tour ID: " + tourId + " | Guide ID: " + guideId);

        if (tourId == -1 || guideId == -1) {
            Log.e("GuideListActivity", "🚨 Invalid tour_id or guide_id received! Exiting...");
            Toast.makeText(this, "Lỗi: Không thể tải dữ liệu, thử lại sau!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadBookingsForTour(tourId, guideId);
    }

    private void loadBookingsForTour(int tourId, int guideId) {
        bookingList = dbHelper.getBookingsForGuideTour(tourId, guideId);

        // 🔥 Lọc danh sách chỉ lấy Booking có trạng thái "Confirmed"
        ArrayList<BookingModel> confirmedBookings = new ArrayList<>();
        for (BookingModel booking : bookingList) {
            if ("Confirmed".equalsIgnoreCase(booking.getStatus())) {
                confirmedBookings.add(booking);
            }
        }

        if (confirmedBookings.isEmpty()) {
            recyclerViewBookings.setVisibility(View.GONE);
            noBookingsText.setVisibility(View.VISIBLE);
        } else {
            recyclerViewBookings.setVisibility(View.VISIBLE);
            noBookingsText.setVisibility(View.GONE);
        }

        bookingAdapter = new GuideBookingAdapter(this, confirmedBookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }
}
