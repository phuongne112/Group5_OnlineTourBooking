package com.example.group5_onlinetourbookingsystem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.GuideListActivity;
import com.example.group5_onlinetourbookingsystem.activities.MyWishlistActivity;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;
import com.example.group5_onlinetourbookingsystem.adapters.TourGuideBookingAdapter;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TourGuideFragment extends Fragment {
    private RecyclerView recyclerViewTours;
    private TourAdapter tourAdapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<TourModel> tourList;
    private SessionManager sessionManager;

    private TourGuideBookingAdapter bookingAdapter;
    private ArrayList<BookingModel> bookingList;

    public TourGuideFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_guide, container, false);

        recyclerViewTours = view.findViewById(R.id.recyclerViewTours);
        dbHelper = new MyDatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        int userId = sessionManager.getUserId();
        int roleId = sessionManager.getUserRoleId();

        if (userId == -1 || roleId == -1) {
            Log.e("TourGuideFragment", "⚠ Lỗi: Không lấy được userId hoặc roleId từ SessionManager!");
            return view;
        }

        if (!dbHelper.isUserGuide(userId)) {
            Log.w("TourGuideFragment", "⚠ User ID " + userId + " chưa là hướng dẫn viên. Đang tự gán...");

            ArrayList<TourModel> availableTours = dbHelper.getAllTours();
            if (!availableTours.isEmpty()) {
                int tourId = availableTours.get(0).getId();
                dbHelper.assignUserToTourGuide(tourId, userId);
            } else {
                Log.e("TourGuideFragment", "❌ Không có tour nào để gán hướng dẫn viên!");
                return view;
            }
        }

        // 👉 Thêm xử lý sự kiện FloatingActionButton
        FloatingActionButton fabWishlist = view.findViewById(R.id.fabWishlist);
        fabWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MyWishlistActivity.class);
            intent.putExtra("guide_id", userId); // Truyền guide ID
            startActivity(intent);
        });

        loadBookingsForGuide(userId);
        return view;
    }


    private void loadTours(int guideId) {
        tourList = dbHelper.getToursByGuide(guideId);

        if (tourList.isEmpty()) {
            Log.e("TourGuideFragment", "Không có tour nào được tìm thấy cho guide ID: " + guideId);
            return;
        }

        tourAdapter = new TourAdapter(requireContext(), tourList, tour -> {
            Log.d("TourGuideFragment", "📌 Tour ID: " + tour.getId() + " | Guide ID: " + guideId);

            Intent intent = new Intent(requireContext(), GuideListActivity.class);
            intent.putExtra("tour_id", tour.getId());
            intent.putExtra("guide_id", guideId);
            startActivity(intent);
        });

        recyclerViewTours.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTours.setAdapter(tourAdapter);
    }

    private void loadBookingsForGuide(int guideId) {
        bookingList = (ArrayList<BookingModel>) dbHelper.getCompletedBookingsForGuide(guideId);

        Log.d("TourGuideFragment", "📌 Số lượng booking có trạng thái COMPLETED: " + bookingList.size());

        if (bookingList.isEmpty()) {
            Log.e("TourGuideFragment", "❌ Không có booking nào có trạng thái COMPLETED.");
            return;
        }

        for (BookingModel booking : bookingList) {
            Log.d("TourGuideFragment", "📌 Tour: " + booking.getTourName()
                    + ", User: " + booking.getName()
                    + ", Adults: " + booking.getAdultCount()
                    + ", Children: " + booking.getChildCount());
        }

        bookingAdapter = new TourGuideBookingAdapter(requireContext(), bookingList, (booking, isFavorite) -> {
            if (isFavorite) {
                dbHelper.addToFavorites(guideId, booking.getTourId());
                Toast.makeText(requireContext(), "✅ Đã thêm vào wishlist thành công", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.removeFromFavorites(guideId, booking.getTourId());
                Toast.makeText(requireContext(), "❌ Đã xóa khỏi wishlist", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerViewTours.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTours.setAdapter(bookingAdapter);
    }
}
