package com.example.group5_onlinetourbookingsystem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.GuideListActivity;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.ArrayList;

public class TourGuideFragment extends Fragment {
    private RecyclerView recyclerViewTours;
    private TourAdapter tourAdapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<TourModel> tourList;
    private SessionManager sessionManager;

    public TourGuideFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_guide, container, false);

        recyclerViewTours = view.findViewById(R.id.recyclerViewTours);

        dbHelper = new MyDatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        int userId = sessionManager.getUserId();
        int roleId = sessionManager.getUserRoleId();

        Log.d("TourGuideFragment", "User ID: " + userId + " | Role ID: " + roleId);

        loadTours(userId, roleId);

        return view;
    }

    private void loadTours(int userId, int roleId) {
        tourList = dbHelper.getToursByGuide(userId, roleId);

        if (tourList == null) {
            tourList = new ArrayList<>();
            Log.e("TourGuideFragment", "Tour list is NULL. Creating an empty list.");
        }


        tourAdapter = new TourAdapter(requireContext(), tourList, tour -> {
            // ✅ Open GuideListActivity with guide's booked tours
            Intent intent = new Intent(requireContext(), GuideListActivity.class);
            intent.putExtra("tour_id", tour.getId());
            startActivity(intent);
        });

        recyclerViewTours.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTours.setAdapter(tourAdapter);
    }
}
