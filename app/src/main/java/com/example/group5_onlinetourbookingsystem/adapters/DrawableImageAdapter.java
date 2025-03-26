package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;

import java.util.List;

public class DrawableImageAdapter extends RecyclerView.Adapter<DrawableImageAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageNames;
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(String imageName);
    }

    public DrawableImageAdapter(Context context, List<String> imageNames, OnImageClickListener listener) {
        this.context = context;
        this.imageNames = imageNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drawable_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageName = imageNames.get(position);
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (resourceId != 0) {
            holder.imageView.setImageResource(resourceId);
        } else {
            holder.imageView.setImageResource(R.drawable.favorites); // Ảnh mặc định nếu không tìm thấy
        }

        holder.itemView.setOnClickListener(v -> listener.onImageClick(imageName));
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}