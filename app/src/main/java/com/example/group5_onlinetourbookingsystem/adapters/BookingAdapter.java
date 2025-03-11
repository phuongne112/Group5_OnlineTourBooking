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

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<BookingModel> bookingList;

    public BookingAdapter(Context context, List<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);
        holder.tvName.setText("Name: " + booking.getTourId()); // Hoặc lấy tên từ bảng `tours`
        holder.tvStatus.setText("Time: " + booking.getStatus());
        holder.tvDate.setText("Date: " + booking.getDate());
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus, tvDate;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTourName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvBookingDate);
        }
    }
}
