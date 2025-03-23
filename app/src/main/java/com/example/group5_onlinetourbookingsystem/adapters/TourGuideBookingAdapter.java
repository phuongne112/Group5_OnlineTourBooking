package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.List;

public class TourGuideBookingAdapter extends RecyclerView.Adapter<TourGuideBookingAdapter.ViewHolder> {

    private Context context;
    private List<BookingModel> bookingList;
    private OnWishlistClickListener wishlistClickListener;

    public interface OnWishlistClickListener {
        void onWishlistClick(BookingModel booking, boolean isFavorite);
    }

    public TourGuideBookingAdapter(Context context, List<BookingModel> bookingList, OnWishlistClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.wishlistClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tourguide_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        holder.tvTourName.setText("Tour: " + booking.getTourName());
        holder.tvUserName.setText("Người đặt: " + booking.getName());
        holder.tvAdults.setText("Người lớn: " + booking.getAdultCount());
        holder.tvChildren.setText("Trẻ em: " + booking.getChildCount());

        // Set đúng icon dựa theo trạng thái yêu thích
        holder.btnWishlist.setImageResource(
                booking.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border
        );

        holder.btnWishlist.setOnClickListener(v -> {
            boolean newState = !booking.isFavorite(); // Toggle trạng thái
            booking.setFavorite(newState); // Cập nhật model

            // Cập nhật icon trái tim
            holder.btnWishlist.setImageResource(
                    newState ? R.drawable.ic_favorite : R.drawable.ic_favorite_border
            );

            if (wishlistClickListener != null) {
                wishlistClickListener.onWishlistClick(booking, newState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvUserName, tvAdults, tvChildren;
        ImageButton btnWishlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvAdults = itemView.findViewById(R.id.tvAdults);
            tvChildren = itemView.findViewById(R.id.tvChildren);
            btnWishlist = itemView.findViewById(R.id.btnAddToWishlist);
        }
    }
}
