package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSubServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sub_services);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserSubServices.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        TextView selectCertainText = (TextView) findViewById(R.id.selectCertainText);
        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton subSelectButton1 = (MaterialButton) findViewById(R.id.subSelectButton1);
        MaterialButton subSelectButton2 = (MaterialButton) findViewById(R.id.subSelectButton2);
        MaterialButton subSelectButton3 = (MaterialButton) findViewById(R.id.subSelectButton3);
        MaterialButton subSelectButton4 = (MaterialButton) findViewById(R.id.subSelectButton4);
        MaterialButton subSelectButton5 = (MaterialButton) findViewById(R.id.subSelectButton5);

        String serviceID = getIntent().getStringExtra("serviceID");
        String serviceName = getIntent().getStringExtra("serviceName");
        String subService1 = getIntent().getStringExtra("subService1");
        String subService2 = getIntent().getStringExtra("subService2");
        String subService3 = getIntent().getStringExtra("subService3");
        String subService4 = getIntent().getStringExtra("subService4");
        String subService5 = getIntent().getStringExtra("subService5");

//        selectCertainText.setText("Select certain service to " + serviceName);
        subSelectButton1.setText(subService1);
        subSelectButton2.setText(subService2);
        subSelectButton3.setText(subService3);
        subSelectButton4.setText(subService4);
        subSelectButton5.setText(subService5);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSubServices.this, UserBookServices.class));
            }
        });

        subSelectButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSubServices.this, UserSelectServiceProvider.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("subService", subService1);
                startActivity(intent);
            }
        });

        subSelectButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSubServices.this, UserSelectServiceProvider.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("subService", subService2);
                startActivity(intent);
            }
        });

        subSelectButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSubServices.this, UserSelectServiceProvider.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("subService", subService3);
                startActivity(intent);
            }
        });

        subSelectButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSubServices.this, UserSelectServiceProvider.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("subService", subService4);
                startActivity(intent);
            }
        });

        subSelectButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSubServices.this, UserSelectServiceProvider.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("subService", subService5);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(UserSubServices.this, UserBookServices.class));
    }
}