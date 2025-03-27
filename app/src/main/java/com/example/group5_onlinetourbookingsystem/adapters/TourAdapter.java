package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TourModel> tourList;
    private OnTourClickListener listener;

    public interface OnTourClickListener {
        void onTourClick(TourModel tour);
    }

    public TourAdapter(Context context, ArrayList<TourModel> tourList, OnTourClickListener listener) {
        this.context = context;
        this.tourList = tourList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TourModel tour = tourList.get(position);

        holder.name.setText(tour.getName());
        holder.price.setText(String.format("$%.2f", tour.getPrice()));
        holder.duration.setText(tour.getDuration() + " days");
        holder.destination.setText(tour.getDestination());
        holder.categoryTextView.setText("Category: " + tour.getCategoryName());

        // ✅ Chỉ hiển thị ảnh từ bộ nhớ trong
        if (tour.getImage() != null && !tour.getImage().isEmpty()) {
            File imageFile = new File(tour.getImage());
            if (imageFile.exists()) {
                Picasso.get().load(imageFile).into(holder.tourImage);
            } else {
                holder.tourImage.setImageResource(R.drawable.favorites); // Ảnh mặc định nếu không tìm thấy ảnh
            }
        } else {
            holder.tourImage.setImageResource(R.drawable.favorites);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTourClick(tour);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, destination, price, duration, categoryTextView;
        ImageView tourImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tour_name);
            destination = itemView.findViewById(R.id.tour_destination);
            price = itemView.findViewById(R.id.tour_price);
            duration = itemView.findViewById(R.id.tour_duration);
            tourImage = itemView.findViewById(R.id.tour_image);
            categoryTextView = itemView.findViewById(R.id.textCategory);
        }
    }

    public void updateTourList(ArrayList<TourModel> newTourList) {
        tourList.clear();
        tourList.addAll(newTourList);
        notifyDataSetChanged();
    }

    public void addTours(ArrayList<TourModel> newTours) {
        int startPosition = tourList.size();
        tourList.addAll(newTours);
        notifyItemRangeInserted(startPosition, newTours.size());
    }
    public void updateTours(ArrayList<TourModel> newTourList) {
        this.tourList.clear();
        this.tourList.addAll(newTourList);
        notifyDataSetChanged();
    }
}
