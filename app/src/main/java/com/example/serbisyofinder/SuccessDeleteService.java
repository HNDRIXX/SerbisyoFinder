package com.example.serbisyofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SuccessDeleteService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_delete_service);

        getSupportActionBar().hide();

        MaterialButton proceedButton = (MaterialButton) findViewById(R.id.proceedButton);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessDeleteService.this, AdminHome.class));
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(SuccessDeleteService.this, ProviderHome.class));
    }
}