package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.List;

public class ManageTourGuideWishListAdapter extends RecyclerView.Adapter<ManageTourGuideWishListAdapter.ViewHolder> {

    private Context context;
    private List<BookingModel> wishlist;
    private OnApplyStatusChangeListener applyStatusChangeListener;

    public interface OnApplyStatusChangeListener {
        void onApplyStatusChanged(BookingModel booking, String newStatus);
    }

    public ManageTourGuideWishListAdapter(Context context, List<BookingModel> wishlist, OnApplyStatusChangeListener listener) {
        this.context = context;
        this.wishlist = wishlist;
        this.applyStatusChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = wishlist.get(position);

        holder.tvTourName.setText("Tour: " + booking.getTourName());
        holder.tvGuideName.setText("Hướng dẫn viên: " + booking.getName());

        // Setup Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                context,
                R.array.apply_status_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerApplyStatus.setAdapter(spinnerAdapter);

        // Set current status
        String currentStatus = booking.getStatus(); // "Pending", "Approved", "Rejected"
        int positionInSpinner = spinnerAdapter.getPosition(currentStatus);
        if (positionInSpinner >= 0) {
            holder.spinnerApplyStatus.setSelection(positionInSpinner);
        }

        // Set color based on status
        updateSpinnerTextColor(holder.spinnerApplyStatus, currentStatus);

        // Handle status change
        holder.spinnerApplyStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
                String selectedStatus = parent.getItemAtPosition(pos).toString();
                updateSpinnerTextColor(holder.spinnerApplyStatus, selectedStatus);
                applyStatusChangeListener.onApplyStatusChanged(booking, selectedStatus);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    private void updateSpinnerTextColor(Spinner spinner, String status) {
        View selectedView = spinner.getSelectedView();
        if (selectedView instanceof TextView) {
            TextView selectedText = (TextView) selectedView;
            switch (status) {
                case "Approved":
                    selectedText.setTextColor(Color.parseColor("#4CAF50")); // Green
                    break;
                case "Rejected":
                    selectedText.setTextColor(Color.RED);
                    break;
                default:
                    selectedText.setTextColor(Color.GRAY);
                    break;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvGuideName;
        Spinner spinnerApplyStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvGuideName = itemView.findViewById(R.id.tvGuideName);
            spinnerApplyStatus = itemView.findViewById(R.id.spinnerApplyStatus);
        }
    }
}
