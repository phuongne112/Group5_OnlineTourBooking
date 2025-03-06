package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> categoryList;
    private OnCategoryClickListener listener;
    private MyDatabaseHelper databaseHelper;
    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryModel category);  // ✅ Chỉ nhận 1 tham số
    }



    public CategoryAdapter(Context context, List<CategoryModel> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
        this.databaseHelper = new MyDatabaseHelper(context); // ✅ Khởi tạo databaseHelper
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        if (category.getImagePath() != null && !category.getImagePath().isEmpty()) {
            int imageResource = context.getResources().getIdentifier(category.getImagePath(), "drawable", context.getPackageName());

            if (imageResource != 0) {
                Uri imageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + imageResource);
                Picasso.get().load(imageUri).placeholder(R.drawable.favorites).into(holder.categoryImage);
            } else {
                holder.categoryImage.setImageResource(R.drawable.favorites);
            }
        } else {
            holder.categoryImage.setImageResource(R.drawable.favorites);
        }

        holder.itemView.setOnClickListener(v -> {
            List<TourModel> tours = databaseHelper.getToursByCategory(category.getId());
            Log.d("CategoryAdapter", "Category ID: " + category.getId() + " - Tours: " + tours.size());
            listener.onCategoryClick(category);  // ✅ Truyền đủ 2 tham số
        });




    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
        }
    }
}
