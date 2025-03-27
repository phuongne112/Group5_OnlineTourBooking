package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.TourDetailActivity;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TourModel> favouriteList;

    public FavouriteAdapter(Context context, ArrayList<TourModel> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TourModel tour = favouriteList.get(position);

        holder.tourName.setText(tour.getName());
        holder.tourDestination.setText("Destination: " + tour.getDestination());
        holder.tourPrice.setText(String.format("$%.2f", tour.getPrice()));
        holder.tourDuration.setText(tour.getDuration() + " days");

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


        // Handle click event to open TourDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TourDetailActivity.class);
            intent.putExtra("tour_id", tour.getId());
            context.startActivity(intent);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tourImage;
        TextView tourName, tourDestination, tourPrice, tourDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tourImage = itemView.findViewById(R.id.tour_image);
            tourName = itemView.findViewById(R.id.tour_name);
            tourDestination = itemView.findViewById(R.id.tour_destination);
            tourPrice = itemView.findViewById(R.id.tour_price);
            tourDuration = itemView.findViewById(R.id.tour_duration);
        }
    }
}
