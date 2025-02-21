package com.example.group5_onlinetourbookingsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private TourAdapter adapter;
    private ArrayList<TourModel> tourList;
    private MyDatabaseHelper databaseHelper;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new MyDatabaseHelper(getContext());
        tourList = databaseHelper.getAllTours();

        if (tourList.isEmpty()) {
            addSampleTours();
            tourList = databaseHelper.getAllTours(); // Lấy lại danh sách sau khi thêm
        }

        adapter = new TourAdapter(getContext(), tourList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void addSampleTours() {
        databaseHelper.addTourWithCategory("Tour Đà Lạt", "Đà Lạt", 150.0, 3, "", "Du lịch núi");
        databaseHelper.addTourWithCategory("Tour Phú Quốc", "Phú Quốc", 200.0, 4, "", "Du lịch biển");
        databaseHelper.addTourWithCategory("Tour Hà Nội", "Hà Nội", 180.0, 2, "", "Du lịch thành phố");
        databaseHelper.addTourWithCategory("Tour Sapa", "Sapa", 220.0, 5, "", "Du lịch núi");
    }

}