package com.example.group5_onlinetourbookingsystem;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

public class BookingFragment extends Fragment {
    private SessionManager sessionManager;
    private ListView listViewBookings;
    private MyDatabaseHelper dbHelper;
    private TextView tvNoBooking;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        listViewBookings = view.findViewById(R.id.listViewBookings);
        tvNoBooking = view.findViewById(R.id.tvNoBooking);
        dbHelper = new MyDatabaseHelper(getContext());
        sessionManager = new SessionManager(requireContext());

        int userId = sessionManager.getUserId();

        if (userId == -1) {
            Log.e("BookingFragment", "Không tìm thấy ID người dùng!");
            tvNoBooking.setVisibility(View.VISIBLE);
            listViewBookings.setVisibility(View.GONE);
        } else {
            loadUserBookings(userId);
        }

        listViewBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String tourName = cursor.getString(cursor.getColumnIndexOrThrow("tour_name"));
                Toast.makeText(getContext(), "Tour: " + tourName + "\nTrạng thái: " + status, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadUserBookings(int userId) {
        Cursor cursor = dbHelper.getUserBookings(userId);

        if (cursor != null && cursor.getCount() > 0) {
            String[] from = {"tour_name", "status", "booking_date"};
            int[] to = {R.id.tvTourName, R.id.tvStatus, R.id.tvBookingDate};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    getContext(),
                    R.layout.list_item_booking,
                    cursor,
                    from,
                    to,
                    0);

            // Custom ViewBinder để set màu và icon
            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.tvStatus) {
                        String status = cursor.getString(columnIndex);
                        TextView tvStatus = (TextView) view;
                        tvStatus.setText(status);

                        ImageView imgStatus = ((ViewGroup) view.getParent()).findViewById(R.id.imgStatusIcon);

                        if (status.equalsIgnoreCase("Pending")) {
                            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                            imgStatus.setImageResource(R.drawable.ic_pending); // icon chờ duyệt
                        } else if (status.equalsIgnoreCase("Confirmed")) {
                            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            imgStatus.setImageResource(R.drawable.ic_approved); // icon đã duyệt
                        } else if (status.equalsIgnoreCase("Cancel")) {
                            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            imgStatus.setImageResource(R.drawable.ic_cancel); // icon hủy
                        } else {
                            tvStatus.setTextColor(getResources().getColor(android.R.color.black));
                            imgStatus.setImageResource(R.drawable.ic_default); // icon mặc định
                        }
                        return true;
                    }
                    return false;
                }
            });

            listViewBookings.setAdapter(adapter);
            listViewBookings.setVisibility(View.VISIBLE);
            tvNoBooking.setVisibility(View.GONE);
        } else {
            listViewBookings.setVisibility(View.GONE);
            tvNoBooking.setVisibility(View.VISIBLE);
        }
    }
}
