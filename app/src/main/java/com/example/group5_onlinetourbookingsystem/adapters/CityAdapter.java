package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.CityModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private List<CityModel> cityList;
    private OnCityClickListener listener;
    private MyDatabaseHelper dbHelper;

    public interface OnCityClickListener {
        void onEditCityClick(CityModel city);
        void onDeleteCityClick(CityModel city);
    }

    public CityAdapter(Context context, List<CityModel> cityList, OnCityClickListener listener) {
        this.context = context;
        this.cityList = cityList;
        this.listener = listener;
        this.dbHelper = new MyDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityModel city = cityList.get(position);
        holder.cityName.setText(city.getName());

        // Xử lý nút Sửa
        holder.btnEditCity.setOnClickListener(v -> listener.onEditCityClick(city));

        // Xử lý nút Xóa
        holder.btnDeleteCity.setOnClickListener(v -> {
            // Kiểm tra xem thành phố có tour liên quan không
            ArrayList<TourModel> tours = dbHelper.getToursByCityId(city.getId());
            if (!tours.isEmpty()) {
                new AlertDialog.Builder(context)
                        .setTitle("Không thể xóa")
                        .setMessage("Không thể xóa thành phố vì có " + tours.size() + " tour liên quan!")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            // Hiển thị thông báo xác nhận xóa
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa thành phố " + city.getName() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> listener.onDeleteCityClick(city))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void updateCityList(List<CityModel> newCityList) {
        this.cityList = newCityList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        ImageButton btnEditCity, btnDeleteCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            btnEditCity = itemView.findViewById(R.id.btnEditCity);
            btnDeleteCity = itemView.findViewById(R.id.btnDeleteCity);
        }
    }
}