package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderBookingRecordsPanel extends AppCompatActivity {

    String mainService, subService, clientName, price, rating, review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_booking_records_panel);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(ProviderBookingRecordsPanel.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        TextView mainServiceValue = (TextView) findViewById(R.id.mainServiceValue);
        TextView subServiceValue = (TextView) findViewById(R.id.subServiceValue);
        TextView clientNameValue = (TextView) findViewById(R.id.clientNameValue);
        TextView priceValue = (TextView) findViewById(R.id.priceValue);
        EditText reviewTextValue = (EditText) findViewById(R.id.reviewTextValue);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        mainService = getIntent().getStringExtra("mainService");
        subService = getIntent().getStringExtra("subService");
        clientName = getIntent().getStringExtra("clientName");
        price = getIntent().getStringExtra("price");
        rating = getIntent().getStringExtra("rating");
        review = getIntent().getStringExtra("review");

        mainServiceValue.setText(mainService);
        subServiceValue.setText(subService);
        clientNameValue.setText(clientName);
        priceValue.setText(price);
        reviewTextValue.setText(review);

        float ratingValue = Float.parseFloat(rating);
        ratingBar.setRating(ratingValue);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ProviderBookingRecordsPanel.this, ProviderBookingRecords.class));
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(ProviderBookingRecordsPanel.this, ProviderBookingRecords.class));
    }
}