package com.example.group5_onlinetourbookingsystem.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.BookingAdapter;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.ArrayList;
import java.util.List;

public class AdminBookingFragment extends Fragment {
    private RecyclerView recyclerViewBookings;
    private TextView tvNoAdminBooking;
    private MyDatabaseHelper dbHelper;
    private BookingAdapter bookingAdapter;
    private List<BookingModel> bookingList = new ArrayList<>();

    public AdminBookingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_booking, container, false);

        recyclerViewBookings = view.findViewById(R.id.recyclerViewBookings);
        tvNoAdminBooking = view.findViewById(R.id.tvNoAdminBooking);
        dbHelper = new MyDatabaseHelper(requireContext());

        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ Truyền true để đánh dấu đây là admin
        bookingAdapter = new BookingAdapter(getContext(), bookingList, dbHelper, true);
        recyclerViewBookings.setAdapter(bookingAdapter);

        loadBookings();
        return view;
    }

    private void loadBookings() {
        bookingList.clear();
//        Cursor cursor = dbHelper.getAllBookingsWithTourInfo();
//        if (cursor == null || cursor.getCount() == 0) {
//            Log.e("AdminBookingFragment", "Cursor is NULL");
//            tvNoAdminBooking.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        if (cursor.getCount() == 0) {
//            Log.e("AdminBookingFragment", "No bookings found in DB");
//            tvNoAdminBooking.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        tvNoAdminBooking.setVisibility(View.GONE);
//
//        while (cursor.moveToNext()) {
//            int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
//            String tourName = cursor.getString(cursor.getColumnIndexOrThrow("tour_name"));
//            if (tourName == null) {
//                Log.e("AdminBookingFragment", "Tour Name is NULL for booking ID: " + bookingId);
//            } else {
//                Log.d("AdminBookingFragment", "Tour Name from DB: " + tourName);
//            }
//            String bookingDate = cursor.getString(cursor.getColumnIndexOrThrow("booking_date"));
//            double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
//            String bookingStatus = cursor.getString(cursor.getColumnIndexOrThrow("booking_status"));
//            String paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("payment_status"));
//
//            bookingList.add(new BookingModel(bookingId, 0, 0, 0, 0, tourName, totalPrice, bookingStatus, paymentStatus, "", bookingDate));
//        }
//
//        cursor.close();
//        bookingAdapter.notifyDataSetChanged();
//    }
        bookingList.addAll(dbHelper.getAllBookingsWithTourInfo());
        List<BookingModel> bookings = dbHelper.getAllBookingsWithTourInfo(); // Lấy danh sách booking

        if (bookings == null || bookings.isEmpty()) {
            Log.e("AdminBookingFragment", "No bookings found in DB");
            tvNoAdminBooking.setVisibility(View.VISIBLE);
        } else {
            tvNoAdminBooking.setVisibility(View.GONE);
            bookingList.addAll(bookings);
        }

        bookingAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
    }
}
