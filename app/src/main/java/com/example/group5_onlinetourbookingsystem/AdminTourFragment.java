package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.group5_onlinetourbookingsystem.activities.AddTourActivity;
import com.example.group5_onlinetourbookingsystem.activities.EditTourActivity;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.R;

import java.util.ArrayList;

public class AdminTourFragment extends Fragment {

    private RecyclerView recyclerView;
    private TourAdapter tourAdapter;
    private ArrayList<TourModel> tourList;
    private MyDatabaseHelper dbHelper;
    private ImageButton btnAddTour;

    public AdminTourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        return inflater.inflate(R.layout.fragment_admin_tour, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewTours);
        btnAddTour = view.findViewById(R.id.btnAddTour);
        dbHelper = new MyDatabaseHelper(getContext());

        // Lấy danh sách tour từ database
        tourList = dbHelper.getAllTours();

        // Setup RecyclerView
        tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
            // Mở màn hình chỉnh sửa tour khi bấm vào tour
            Intent intent = new Intent(getActivity(), EditTourActivity.class);
            intent.putExtra("TOUR_ID", tour.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tourAdapter);

        // Xử lý nút thêm tour
        btnAddTour.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTourActivity.class);
            startActivity(intent);

        });

    }
}
