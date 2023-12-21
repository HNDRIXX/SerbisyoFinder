package com.example.serbisyofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SuccessSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_sign_up);

        getSupportActionBar().hide();

        MaterialButton proceedSignUpButton = (MaterialButton) findViewById(R.id.proceedSignUpButton);

        proceedSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessSignUp.this, MainActivity.class));
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(SuccessSignUp.this, MainActivity.class));
    }
}