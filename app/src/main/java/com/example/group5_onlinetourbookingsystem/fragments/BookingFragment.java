package com.example.group5_onlinetourbookingsystem.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.UserBookingAdapter;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {
    private RecyclerView recyclerViewUserBookings;
    private TextView tvNoBooking;
    private MyDatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private List<BookingModel> bookingList;
    private UserBookingAdapter userBookingAdapter;

    public BookingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerViewUserBookings = view.findViewById(R.id.recyclerViewUserBookings);
        tvNoBooking = view.findViewById(R.id.tvNoBooking);

        dbHelper = new MyDatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        bookingList = new ArrayList<>();
        userBookingAdapter = new UserBookingAdapter(requireContext(), bookingList);

        recyclerViewUserBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewUserBookings.setAdapter(userBookingAdapter);

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Log.e("BookingFragment", "Không tìm thấy ID người dùng!");
            showEmptyView(true);
        } else {
            loadUserBookings(userId);
        }

        return view;
    }

    private void loadUserBookings(int userId) {
        bookingList.clear();
        Cursor cursor;

        boolean isAdmin = sessionManager.getUserRoleId() == 2;

        if (isAdmin) {
            cursor = dbHelper.getUserBookingsWithPayment(userId);
        } else {
            cursor = dbHelper.getUserBookings(userId);
        }

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String tourName = cursor.getString(cursor.getColumnIndexOrThrow("tour_name"));
                String bookingDate = cursor.getString(cursor.getColumnIndexOrThrow("booking_date"));
                String bookingStatus = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
                String paymentStatus = "";

                if (isAdmin) {
                    paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("payment_status"));
                }

                BookingModel booking = new BookingModel(id, 0, 0, 0, 0, "", totalPrice, bookingStatus, paymentStatus, "", bookingDate);
                booking.setTourName(tourName); // ✅ Gán tên tour đúng cách
                bookingList.add(booking);
            }
            cursor.close();
            showEmptyView(false);
        } else {
            showEmptyView(true);
        }

        userBookingAdapter.notifyDataSetChanged();
    }

    private void showEmptyView(boolean isEmpty) {
        tvNoBooking.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerViewUserBookings.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
