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
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoryModel> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryModel category);
    }

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
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
            // Lấy resource ID từ drawable
            int imageResource = context.getResources().getIdentifier(category.getImagePath(), "drawable", context.getPackageName());

            if (imageResource != 0) {
                // Load ảnh bằng URI thay vì đường dẫn
                Uri imageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + imageResource);
                Picasso.get().load(imageUri).placeholder(R.drawable.favorites).into(holder.categoryImage);
            } else {
                holder.categoryImage.setImageResource(R.drawable.favorites);
            }
        } else {
            holder.categoryImage.setImageResource(R.drawable.favorites);
        }
        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));

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
            categoryImage = itemView.findViewById(R.id.category_image); // Thêm ImageView
        }
    }
}
