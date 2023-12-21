package com.example.serbisyofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SuccessConfirmedBooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_confirmed_booking);

        getSupportActionBar().hide();

        MaterialButton greatButton = (MaterialButton) findViewById(R.id.greatButton);

        greatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessConfirmedBooking.this, ProviderHome.class));
                finish();
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(SuccessConfirmedBooking.this, ProviderHome.class));
    }
}