package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.HomeHorModel;

import java.util.ArrayList;
import java.util.List;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {
    Context context;
    ArrayList id, name, email, phone, image;
    public HomeHorAdapter(Context context,
                          ArrayList id,
                          ArrayList name,
                          ArrayList email,
                          ArrayList phone,
                          ArrayList image){
        this.context = context;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }
    @NonNull
    @Override
    public HomeHorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHorAdapter.ViewHolder holder, int position) {
       holder.user_id_txt.setText(String.valueOf(id.get(position)));
        holder.user_name.setText(String.valueOf(name.get(position)));
        holder.email.setText(String.valueOf(email.get(position)));
        holder.phone.setText(String.valueOf(phone.get(position)));
        holder.image.setText(String.valueOf(image.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView user_id_txt, user_name, email,phone,image;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            user_id_txt = itemView.findViewById(R.id.user_id_txt);
            user_name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            image = itemView.findViewById(R.id.image);
        }
    }
}
