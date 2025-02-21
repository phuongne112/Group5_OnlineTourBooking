package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

public class AddActivity extends AppCompatActivity {
    EditText edtName, edtEmail,edtPhone,edtAdditionalInfo;
    Button add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAdditionalInfo = findViewById(R.id.edtAdditionalInfo);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addUser(edtName.getText().toString().trim(),
                        edtEmail.getText().toString().trim(),
                        edtPhone.getText().toString().trim(),
                        edtAdditionalInfo.getText().toString().trim());
             }
        });
    }
}