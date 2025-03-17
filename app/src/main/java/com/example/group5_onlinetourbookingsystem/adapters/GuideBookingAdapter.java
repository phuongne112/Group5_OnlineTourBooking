package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.List;

public class GuideBookingAdapter extends RecyclerView.Adapter<GuideBookingAdapter.ViewHolder> {
    private Context context;
    private List<BookingModel> bookingList;

    public GuideBookingAdapter(Context context, List<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_guide_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        // 🔥 Chỉ hiển thị các Booking có trạng thái "Confirmed"
        if (!"Confirmed".equalsIgnoreCase(booking.getStatus())) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0)); // Ẩn item
            return;
        }

        holder.tvTourName.setText("Tour: " + booking.getName());
        holder.tvPassengerCount.setText("Hành khách: " + (booking.getAdultCount() + booking.getChildCount()));
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvPassengerCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvPassengerCount = itemView.findViewById(R.id.tvPassengerCount);
        }
    }
}
