package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import java.util.ArrayList;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TourModel> tourList;

    public TourAdapter(Context context, ArrayList<TourModel> tourList) {
        this.context = context;
        this.tourList = tourList;
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
        holder.name.setText("Tên Tour: " + tour.getName());
        holder.destination.setText("Điểm đến: " + tour.getDestination());
        holder.price.setText("Giá: $" + tour.getPrice());
        holder.duration.setText("Thời gian: " + tour.getDuration() + " ngày");
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, destination, price, duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tour_name);
            destination = itemView.findViewById(R.id.tour_destination);
            price = itemView.findViewById(R.id.tour_price);
            duration = itemView.findViewById(R.id.tour_duration);
        }
    }
}
