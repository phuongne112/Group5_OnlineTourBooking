package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class UserBookingAdapter extends RecyclerView.Adapter<UserBookingAdapter.ViewHolder> {
    private final Context context;
    private final List<BookingModel> bookingList;
    private final int roleId;

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
        holder.tvAdultCount.setText("Người lớn: " + booking.getAdultCount());
        holder.tvBookingDate.setText("Ngày đặt: " + booking.getDate());
        holder.tvTotalPrice.setText(String.format(Locale.getDefault(), "Tổng: %,.2f $", booking.getTotalPrice()));
        holder.tvBookingStatus.setText("Trạng thái: " + booking.getStatus());

        // ✅ Chỉ admin mới thấy payment status
        if (roleId == 2 && booking.getPaymentStatus() != null && !booking.getPaymentStatus().isEmpty()) {
            holder.tvPaymentStatus.setVisibility(View.VISIBLE);
            holder.tvPaymentStatus.setText("Thanh toán: " + booking.getPaymentStatus());
        } else {
            holder.tvPaymentStatus.setVisibility(View.GONE);
        }

        // ✅ Load hình ảnh tour
        if (booking.getTourImage() != null && !booking.getTourImage().isEmpty()) {
            String imageName = booking.getTourImage().replace(".jpg", "").replace(".png", "").toLowerCase().trim();
            int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            holder.imgTourImage.setImageResource(imageResId != 0 ? imageResId : R.drawable.placeholder_image);
        } else {
            holder.imgTourImage.setImageResource(R.drawable.placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvTotalPrice, tvBookingStatus, tvPaymentStatus, tvAdultCount,tvBookingDate;
        ImageView imgTourImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            tvAdultCount = itemView.findViewById(R.id.tvAdultCount);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);// Thêm dòng này
            imgTourImage = itemView.findViewById(R.id.imgTourImage);
        }
    }
}
