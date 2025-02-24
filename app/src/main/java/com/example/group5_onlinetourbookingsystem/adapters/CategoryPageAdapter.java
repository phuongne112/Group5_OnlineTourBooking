package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;

import java.util.List;

public class CategoryPageAdapter extends RecyclerView.Adapter<CategoryPageAdapter.PageViewHolder> {
    private Context context;
    private List<List<CategoryModel>> pages;
    private CategoryAdapter.OnCategoryClickListener listener;

    public CategoryPageAdapter(Context context, List<List<CategoryModel>> pages, CategoryAdapter.OnCategoryClickListener listener) {
        this.context = context;
        this.pages = pages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        List<CategoryModel> categoryPage = pages.get(position);

        CategoryAdapter adapter = new CategoryAdapter(context, categoryPage, listener);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerViewCategoryPage);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
        }
    }
}
