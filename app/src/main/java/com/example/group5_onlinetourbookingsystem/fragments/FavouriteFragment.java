package com.example.group5_onlinetourbookingsystem.fragments;

import android.os.Bundle;
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
import com.example.group5_onlinetourbookingsystem.adapters.FavouriteAdapter;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private ArrayList<TourModel> favouriteList;
    private MyDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView emptyMessage;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.recycler_favourite);
        emptyMessage = view.findViewById(R.id.empty_message);

        databaseHelper = new MyDatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFavourites();

        return view;
    }

    private void loadFavourites() {
        int userId = sessionManager.getUserId();
        favouriteList = databaseHelper.getFavoriteTours(userId);

        if (favouriteList.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
            recyclerView.setAdapter(favouriteAdapter);
        }
        if (favouriteAdapter == null) {
            favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
            recyclerView.setAdapter(favouriteAdapter);
        } else {
            favouriteAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadFavourites();
    }
}
