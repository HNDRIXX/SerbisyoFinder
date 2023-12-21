package com.example.serbisyofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SuccessBookService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_book_service);

        getSupportActionBar().hide();

        MaterialButton okayButton = (MaterialButton) findViewById(R.id.okayBookSuccessButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessBookService.this, UserHome.class));
            }
        });
    }
}