package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.UserModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<UserModel> userList;
    private OnUserActionListener listener;

    // ✅ Constructor chỉ cần một lần khai báo OnUserActionListener
    public UserAdapter(Context context, List<UserModel> userList, OnUserActionListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.tvUserName.setText(user.getName());

        if (user.getStatus().equals("banned")) {
            holder.btnOptions.setImageResource(R.drawable.ic_unban); // Hiển thị icon mở khóa
        } else {
            holder.btnOptions.setImageResource(R.drawable.ic_ban); // Hiển thị icon cấm
        }

        holder.btnOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnOptions);
            popupMenu.getMenuInflater().inflate(R.menu.menu_user_options, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_ban) {
                    listener.onBanUser(user);
                    return true;
                } else if (item.getItemId() == R.id.action_unban) {
                    listener.onUnbanUser(user);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    // ✅ ViewHolder cho từng item
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        ImageView btnOptions;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnOptions = itemView.findViewById(R.id.btnOptions);
        }
    }

    // ✅ Interface để xử lý sự kiện Ban/Unban (Chỉ khai báo **một lần**)
    public interface OnUserActionListener {
        void onBanUser(UserModel user);
        void onUnbanUser(UserModel user);
    }
}
