package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
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

import java.util.ArrayList;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TourModel> tourList;
    private OnTourClickListener listener;

    public interface OnTourClickListener {
        void onTourClick(TourModel tour);
    }
    public TourAdapter(Context context, ArrayList<TourModel> tourList) {
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
        holder.destination.setText(tour.getDestination());
        holder.categoryTextView.setText("Category: " + tour.getCategoryName());

        // Lấy resource ID từ drawable
        int imageResource = context.getResources().getIdentifier(tour.getImage(), "drawable", context.getPackageName());

        if (imageResource != 0) {
            // Load ảnh bằng URI thay vì ID
            Uri imageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + imageResource);
            Picasso.get().load(imageUri).into(holder.TourImage);
        } else {
            Picasso.get().load(R.drawable.favorites).into(holder.TourImage);
        }

        holder.itemView.setOnClickListener(v -> listener.onTourClick(tour));
    }


    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, destination, price, duration, categoryTextView;;
        ImageView TourImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tour_name);
            destination = itemView.findViewById(R.id.tour_destination);
            price = itemView.findViewById(R.id.tour_price);
            duration = itemView.findViewById(R.id.tour_duration);
            TourImage = itemView.findViewById(R.id.tour_image);
            categoryTextView = itemView.findViewById(R.id.textCategory);
        }
    }
    public void updateTourList(ArrayList<TourModel> newTourList) {
        tourList.clear();
        tourList.addAll(newTourList);
        notifyDataSetChanged();
    }

}
