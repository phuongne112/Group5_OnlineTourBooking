package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourImageAdapter extends RecyclerView.Adapter<TourImageAdapter.TourImageViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public TourImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public TourImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour_image, parent, false);
        return new TourImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // Sử dụng Picasso để load ảnh từ URL
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.category) // Ảnh tạm khi đang load
                .error(R.drawable.trends2) // Ảnh hiển thị khi lỗi
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class TourImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public TourImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tour_image_item);
        }
    }
}
