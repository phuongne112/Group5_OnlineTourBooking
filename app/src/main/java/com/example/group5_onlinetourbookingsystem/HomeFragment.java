package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.TourDetailActivity;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewCategories, recyclerViewTours;
    private CategoryAdapter categoryAdapter;
    private TourAdapter tourAdapter;
    private ArrayList<CategoryModel> categoryList;
    private ArrayList<TourModel> tourList;
    private MyDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView textViewUserName;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getContext() == null) return view;

        sessionManager = new SessionManager(requireContext());
        databaseHelper = new MyDatabaseHelper(requireContext());
        // Search
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                searchTours(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // 👉 Cấu hình RecyclerView danh mục
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoryList = databaseHelper.getAllCategories();
        if (categoryList.isEmpty()) {
            addSampleCategories();
            categoryList = databaseHelper.getAllCategories();
        }

        categoryAdapter = new CategoryAdapter(getContext(), categoryList, category -> {
            // Xử lý khi nhấn vào danh mục
        });

        recyclerViewCategories.setAdapter(categoryAdapter);

        // 👉 Cấu hình RecyclerView tour
        recyclerViewTours = view.findViewById(R.id.recycler_view);
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(getContext()));

        tourList = databaseHelper.getAllTours();
        if (tourList.isEmpty()) {
            addSampleTours();
            tourList = databaseHelper.getAllTours();
        }

        tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
            Intent intent = new Intent(getContext(), TourDetailActivity.class);
            intent.putExtra("tour_id", tour.getId());
            startActivity(intent);
        });

        recyclerViewTours.setAdapter(tourAdapter);

        return view;
    }

    private void addSampleCategories() {
        databaseHelper.addCategory("Du lịch núi", "mountain");
        databaseHelper.addCategory("Du lịch biển", "sea");
        databaseHelper.addCategory("Du lịch thành phố", "city");
        databaseHelper.addCategory("Du lịch sinh thái", "eco");
        databaseHelper.addCategory("Du lịch văn hóa", "cultural");
        databaseHelper.addCategory("Du lịch phiêu lưu", "adventure");
    }

    private void addSampleTours() {
        databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 1, 150.0, 3, "dalat_tour", 1, "2025-03-10 08:00:00");
        databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 2, 200.0, 4, "phuquoc_tour", 2, "2025-03-12 09:30:00");
        databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 3, 180.0, 3, "hanoi_tour", 3, "2025-03-15 07:45:00");
        databaseHelper.addTour("Tour Đà Nẵng", "Đà Nẵng", 4, 220.0, 5, "danang_tour", 4, "2025-03-18 10:15:00");
        databaseHelper.addTour("Tour Nha Trang", "Nha Trang", 5, 190.0, 4, "nhatrang_tour", 5, "2025-03-20 08:30:00");
        databaseHelper.addTour("Tour Sapa", "Sapa", 6, 170.0, 3, "sapa_tour", 6, "2025-03-25 06:00:00");
    }

    private void searchTours(String query) {
        tourList.clear();
        if (query.isEmpty()) {
            tourList.addAll(databaseHelper.getAllTours());
        } else {
            tourList.addAll(databaseHelper.searchTours(query));
        }
        tourAdapter.notifyDataSetChanged();
    }


}
