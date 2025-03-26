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

public class SimpleTourAdapter extends RecyclerView.Adapter<SimpleTourAdapter.SimpleTourViewHolder> {

    private Context context;
    private ArrayList<TourModel> tourList;

    public SimpleTourAdapter(Context context, ArrayList<TourModel> tourList) {
        this.context = context;
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public SimpleTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_simple_tour, parent, false);
        return new SimpleTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleTourViewHolder holder, int position) {
        TourModel tour = tourList.get(position);
        holder.tvTourName.setText(tour.getName());
        holder.tvTourPrice.setText(String.format("$%.2f", tour.getPrice())); // Định dạng giá tiền giống ảnh
        holder.tvTourDuration.setText(String.format("%d days", tour.getDuration())); // Hiển thị duration
        holder.tvTourCategory.setText(String.format("Category: %s", tour.getCategoryName())); // Hiển thị category
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    // Phương thức cập nhật danh sách tour
    public void updateTourList(ArrayList<TourModel> newTourList) {
        this.tourList = newTourList;
        notifyDataSetChanged();
    }

    static class SimpleTourViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvTourPrice, tvTourDuration, tvTourCategory;

        public SimpleTourViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvTourPrice = itemView.findViewById(R.id.tvTourPrice);
            tvTourDuration = itemView.findViewById(R.id.tvTourDuration);
            tvTourCategory = itemView.findViewById(R.id.tvTourCategory);
        }
    }
}