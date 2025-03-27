package com.example.group5_onlinetourbookingsystem.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.TourDetailActivity;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;


public class TourFragment extends Fragment {

    private RecyclerView recyclerViewTours, recyclerViewCategories;
    private TourAdapter tourAdapter;
    private ArrayList<CategoryModel> categoryList;
    private CategoryAdapter categoryAdapter;
    private ArrayList<TourModel> tourList;
    private MyDatabaseHelper databaseHelper;

    public TourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);

        if (getContext() == null) return view;

        databaseHelper = new MyDatabaseHelper(getContext());
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        // Cấu hình RecyclerView danh mục
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryList = databaseHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, category -> {
            Log.d("HomeFragment", "Clicked category: " + category.getName());
            filterToursByCategory(category.getId());
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

    private void addSampleTours() {
        databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 1, 150.0, 3, "dalat_tour", 1, "2025-03-10 08:00:00",
                "Thưởng thức khí hậu mát mẻ và cảnh đẹp thơ mộng của Đà Lạt.");
        databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 2, 200.0, 4, "phuquoc_tour", 2, "2025-03-12 09:30:00",
                "Khám phá hòn đảo ngọc với bãi biển tuyệt đẹp và hải sản tươi ngon.");
        databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 3, 180.0, 3, "hanoi_tour", 3, "2025-03-15 07:45:00",
                "Trải nghiệm văn hóa, lịch sử thủ đô với 36 phố phường và Hồ Gươm.");
        databaseHelper.addTour("Tour Đà Nẵng", "Đà Nẵng", 4, 220.0, 5, "danang_tour", 4, "2025-03-18 10:15:00",
                "Tận hưởng biển Mỹ Khê và tham quan Bà Nà Hills nổi tiếng.");
        databaseHelper.addTour("Tour Nha Trang", "Nha Trang", 5, 190.0, 4, "nhatrang_tour", 5, "2025-03-20 08:30:00",
                "Tham quan vịnh biển đẹp nhất Việt Nam và thưởng thức hải sản.");
        databaseHelper.addTour("Tour Sapa", "Sapa", 6, 170.0, 3, "sapa_tour", 6, "2025-03-25 06:00:00",
                "Chinh phục đỉnh Fansipan và khám phá văn hóa dân tộc thiểu số.");
    }
    private void filterToursByCategory(int categoryId) {
        tourList.clear();
        if (categoryId == -1) { // Nếu chọn "Tất cả danh mục"
            tourList.addAll(databaseHelper.getAllTours());
        } else {
            tourList.addAll(databaseHelper.getToursByCategory(categoryId));
        }
        tourAdapter.notifyDataSetChanged();
    }

}
