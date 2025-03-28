package com.example.group5_onlinetourbookingsystem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.group5_onlinetourbookingsystem.MainActivity;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.ChangePasswordActivity;
import com.example.group5_onlinetourbookingsystem.activities.ContactUsActivity;
import com.example.group5_onlinetourbookingsystem.activities.HelpCenterActivity;
import com.example.group5_onlinetourbookingsystem.activities.HomePage;
import com.example.group5_onlinetourbookingsystem.activities.LegalActivity;
import com.example.group5_onlinetourbookingsystem.activities.UserProfileActivity;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

public class ProfileFragment extends Fragment {
    private SessionManager sessionManager;
    private TextView textViewUserName;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (getContext() == null) return view;

        sessionManager = new SessionManager(getContext());

        textViewUserName = view.findViewById(R.id.textViewUserName);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        if (sessionManager.isLoggedIn()) {
            String email = sessionManager.getUserName();
            String displayName = email.split("@")[0];
            textViewUserName.setText("Hello " + displayName);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        } else {
            textViewUserName.setText("Hello Guest");
            btnLogout.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), MainActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Intent intent = new Intent(requireActivity(), HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        LinearLayout userProfile = view.findViewById(R.id.userProfile);
        LinearLayout contactUs = view.findViewById(R.id.contactUs);
        LinearLayout legal = view.findViewById(R.id.legal);
        LinearLayout changePassword = view.findViewById(R.id.changePassword);

        LinearLayout helpCenter = view.findViewById(R.id.helpCenter);

        userProfile.setOnClickListener(v -> openActivity(UserProfileActivity.class));
        contactUs.setOnClickListener(v -> openActivity(ContactUsActivity.class));
        legal.setOnClickListener(v -> openActivity(LegalActivity.class));

        helpCenter.setOnClickListener(v -> openActivity(HelpCenterActivity.class));


        if (sessionManager.getUserRoleId() == 2) {
            changePassword.setVisibility(View.GONE);
        } else {
            changePassword.setVisibility(View.VISIBLE);
            changePassword.setOnClickListener(v -> openActivity(ChangePasswordActivity.class));
        }

        return view;
    }

    private void openActivity(Class<?> activityClass) {
        if (getContext() != null) {
            startActivity(new Intent(getContext(), activityClass));
        }
    }



}
