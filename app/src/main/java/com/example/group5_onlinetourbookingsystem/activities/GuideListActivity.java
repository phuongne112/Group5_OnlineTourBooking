package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView; // ✅ Import for noBookingsText

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.BookingAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.ArrayList;

public class GuideListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<BookingModel> bookingList;
    private int tourId;
    private TextView noBookingsText; // ✅ TextView for "No bookings available"

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        // ✅ Initialize Views Properly
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        noBookingsText = findViewById(R.id.noBookingsText); // Make sure this ID exists in XML

        dbHelper = new MyDatabaseHelper(this);

        tourId = getIntent().getIntExtra("tour_id", -1);

        if (tourId == -1) {
            Log.e("GuideListActivity", "Invalid tour_id received!");
            finish();
        }

        loadBookingsForTour(tourId);
    }

    private void loadBookingsForTour(int tourId) {
        bookingList = dbHelper.getBookingsForTour(tourId);

        if (bookingList == null || bookingList.isEmpty()) {
            Log.e("GuideListActivity", "No bookings found for tour ID: " + tourId);
            bookingList = new ArrayList<>();

            // ✅ Prevent NullPointerException
            if (recyclerViewBookings != null) {
                recyclerViewBookings.setVisibility(View.GONE);
            }
            if (noBookingsText != null) {
                noBookingsText.setVisibility(View.VISIBLE);
            }
        } else {
            if (recyclerViewBookings != null) {
                recyclerViewBookings.setVisibility(View.VISIBLE);
            }
            if (noBookingsText != null) {
                noBookingsText.setVisibility(View.GONE);
            }
        }

        // ✅ Corrected constructor call by passing `dbHelper`
        bookingAdapter = new BookingAdapter(this, bookingList, dbHelper);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }
}
