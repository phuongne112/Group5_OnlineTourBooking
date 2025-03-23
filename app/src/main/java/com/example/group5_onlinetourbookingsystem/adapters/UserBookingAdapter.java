package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.List;
public class UserBookingAdapter extends RecyclerView.Adapter<UserBookingAdapter.ViewHolder> {
    private Context context;
    private List<BookingModel> bookingList;
    private int roleId;

    public UserBookingAdapter(Context context, List<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
        SessionManager sessionManager = new SessionManager(context);
        this.roleId = sessionManager.getUserRoleId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        holder.tvTourName.setText("Tour: " + booking.getTourName());
        holder.tvTotalPrice.setText(String.format("Tổng: %,d $", (int) booking.getTotalPrice()));
        holder.tvBookingStatus.setText("Trạng thái: " + booking.getStatus());

        // ✅ Chỉ admin mới thấy payment status
        if (roleId == 2 && booking.getPaymentStatus() != null && !booking.getPaymentStatus().isEmpty()) {
            holder.tvPaymentStatus.setVisibility(View.VISIBLE);
            holder.tvPaymentStatus.setText("Thanh toán: " + booking.getPaymentStatus());
        } else {
            holder.tvPaymentStatus.setVisibility(View.GONE);
        }

        String imageName = "placeholder_image";
        if (booking.getTourImage() != null && !booking.getTourImage().isEmpty()) {
            imageName = booking.getTourImage().replace(".jpg", "").replace(".png", "").toLowerCase().trim();
        }

        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.imgTourImage.setImageResource(imageResId != 0 ? imageResId : R.drawable.placeholder_image);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvTotalPrice, tvBookingStatus, tvPaymentStatus;
        ImageView imgTourImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            imgTourImage = itemView.findViewById(R.id.imgTourImage);
        }
    }
}
