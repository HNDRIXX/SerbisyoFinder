package com.example.serbisyofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SuccessConfirmedPending extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_confirmed_pending);

        getSupportActionBar().hide();

        MaterialButton greatButton = (MaterialButton) findViewById(R.id.greatButton);

        greatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessConfirmedPending.this, ProviderHome.class));
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(SuccessConfirmedPending.this, ProviderHome.class));
    }
}