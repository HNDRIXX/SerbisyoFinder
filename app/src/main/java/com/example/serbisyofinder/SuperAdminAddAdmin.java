package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SuperAdminAddAdmin extends AppCompatActivity {

    RelativeLayout serviceProviderInputWrapper;
    EditText nameValue, addressValue, phoneNumValue, emailValue, passwordValue, occupationValue;
    Spinner roleDropDown, genderDropDown, mainServicesDropDown;
    MaterialButton doneSignUpButton, confirmSignUpButton;

    FirebaseAuth fAuth;
    DatabaseReference databaseReference;

    ProgressBar progressBar;
    TextView progressBarText;
    ImageView progressBarBackground;

    String[] mainServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_add_admin);

        getSupportActionBar().hide();

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        nameValue = (EditText) findViewById(R.id.nameValue);
        addressValue = (EditText) findViewById(R.id.addressValue);
        phoneNumValue = (EditText) findViewById(R.id.phoneNumValue);
        emailValue = (EditText) findViewById(R.id.emailValue);
        passwordValue = (EditText) findViewById(R.id.passwordValue);

        doneSignUpButton = (MaterialButton) findViewById(R.id.doneSignUpButton);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);
        progressBarBackground = findViewById(R.id.progressBarBackground);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Categories");

        doneSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpEvent();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuperAdminAddAdmin.this, SuperAdminHome.class));
            }
        });

    }

    public void signUpEvent() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        String accID = stringBuilder.toString().trim();
        String name = nameValue.getText().toString().trim();
        String address = addressValue.getText().toString().trim();
        String gender = "Admin";
        String phoneNum = phoneNumValue.getText().toString().trim();
        String email = emailValue.getText().toString().trim();
        String password = passwordValue.getText().toString().trim();
        String role = "Admin";
        String occupation = "Admin";
        String mainService = "Select Main Service";
        String earnings = "0";
        String countBookings = "0";
        String starRating = "0";

        if (TextUtils.isEmpty(name)){
            nameValue.setError("Name Required");
            return;
        }

        if (TextUtils.isEmpty(address)){
            addressValue.setError("Address Required");
            return;
        }
        if (TextUtils.isEmpty(phoneNum)){
            phoneNumValue.setError("Phone Number Required");
            return;
        }

        if (TextUtils.isEmpty(email)){
            emailValue.setError("Email Required");
            return;
        }

        if (TextUtils.isEmpty(password)){
            passwordValue.setError("Password Required");
            return;
        }

        if (password.length() < 6){
            passwordValue.setError("Password must be 6 characters above");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Account account = new Account(accID, name, address, gender, phoneNum, email, password, role, occupation, starRating,  mainService, earnings,  countBookings);

                    FirebaseDatabase.getInstance("https://serbisyofinder-f09d8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Account")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        progressBarText.setVisibility(View.VISIBLE);
                                        progressBarBackground.setVisibility(View.VISIBLE);

                                        startActivity(new Intent(SuperAdminAddAdmin.this, SuperAdminHome.class));
                                        Toast.makeText(SuperAdminAddAdmin.this, "New Admin Created!", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(SuperAdminAddAdmin.this, "Sign-Up Failed!"
                                                + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }else
                    Toast.makeText(SuperAdminAddAdmin.this, "Failed! - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                progressBarText.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(SuperAdminAddAdmin.this, SuperAdminHome.class));
    }
}