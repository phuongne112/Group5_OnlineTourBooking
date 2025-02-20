package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.HomeHorAdapter;
import com.example.group5_onlinetourbookingsystem.models.HomeHorModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    RecyclerView recyclerView;
    MyDatabaseHelper myDB;
    ArrayList<String> id, name, email, phone, image;
    HomeHorAdapter homeHorAdapter;
    FloatingActionButton add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, AddActivity.class);
                startActivity(intent);
            }
        });
        myDB = new MyDatabaseHelper(HomePage.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        email = new ArrayList<>();
        phone = new ArrayList<>();
        image = new ArrayList<>();
        storeDataInArrays();
        homeHorAdapter = new HomeHorAdapter(HomePage.this,id,name,email,phone,image);
        recyclerView.setAdapter(homeHorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
    }
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0 ){
            Toast.makeText(this,"no data",Toast.LENGTH_SHORT).show();

        }else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(1));
                name.add(cursor.getString(2));
                email.add(cursor.getString(3));
                phone.add(cursor.getString(4));
                image.add(cursor.getString(0));
            }
        }

    }
}