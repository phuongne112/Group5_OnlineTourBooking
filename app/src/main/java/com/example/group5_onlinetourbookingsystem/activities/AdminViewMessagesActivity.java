package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.MessageAdapter;
import com.example.group5_onlinetourbookingsystem.models.Message;

import java.util.ArrayList;
import java.util.List;

public class AdminViewMessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_messages);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("User Messages");
        }

        recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageAdapter(messageList, this); // ✅ Thêm context
        recyclerView.setAdapter(adapter);

        // TODO: Lấy danh sách tin nhắn từ database
        loadMessages();
    }

    private void loadMessages() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        messageList.clear(); // Xóa dữ liệu cũ nếu có
        messageList.addAll(dbHelper.getAllHelpCenterMessages()); // ✅ Lấy từ DB
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
