package com.example.group5_onlinetourbookingsystem.fragments;

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
        return inflater.inflate(R.layout.fragment_admin_tour, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewTours);
        btnAddTour = view.findViewById(R.id.btnAddTour);
        dbHelper = new MyDatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ 초기 데이터 로드
        loadTours();

        // ✅ "추가" 버튼 클릭 시 AddTourActivity 실행
        btnAddTour.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTourActivity.class);
            startActivity(intent);
        });
    }

    // ✅ DB에서 투어 리스트를 다시 로드하는 함수
    private void loadTours() {
        tourList = dbHelper.getAllTours();

        if (tourAdapter == null) {
            tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
                // ✅ "수정" 화면으로 이동
                Intent intent = new Intent(getActivity(), EditTourActivity.class);
                intent.putExtra("TOUR_ID", tour.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(tourAdapter);
        } else {
            // ✅ 기존 Adapter가 있으면 데이터만 갱신
            tourAdapter.updateTours(tourList);
        }
    }

    // ✅ 자동 새로고침: 사용자가 화면으로 돌아올 때마다 리스트 갱신
    @Override
    public void onResume() {
        super.onResume();
        loadTours();
    }
}
