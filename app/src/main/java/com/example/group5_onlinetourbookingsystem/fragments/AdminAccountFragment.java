package com.example.group5_onlinetourbookingsystem.fragments;

import android.os.Bundle;
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
import com.example.group5_onlinetourbookingsystem.adapters.UserAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.models.UserModel;

import java.util.ArrayList;

public class AdminAccountFragment extends Fragment implements UserAdapter.OnUserActionListener {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<UserModel> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_account, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new MyDatabaseHelper(getContext());
        userList = dbHelper.getAllUsers();

        adapter = new UserAdapter(getContext(), userList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onBanUser(UserModel user) {
        dbHelper.updateUserStatus(user.getId(), "banned");
        Toast.makeText(getContext(), user.getName() + " đã bị cấm!", Toast.LENGTH_SHORT).show();
        refreshUserList();
    }

    @Override
    public void onUnbanUser(UserModel user) {
        dbHelper.updateUserStatus(user.getId(), "active");
        Toast.makeText(getContext(), user.getName() + " đã được mở khóa!", Toast.LENGTH_SHORT).show();
        refreshUserList();
    }

    private void refreshUserList() {
        userList.clear();
        userList.addAll(dbHelper.getAllUsers());
        adapter.notifyDataSetChanged();
    }
}

