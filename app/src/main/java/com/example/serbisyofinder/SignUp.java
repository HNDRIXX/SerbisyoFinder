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

public class SignUp extends AppCompatActivity {

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
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        serviceProviderInputWrapper = (RelativeLayout) findViewById(R.id.serviceProviderInputWrapper);
        nameValue = (EditText) findViewById(R.id.nameValue);
        addressValue = (EditText) findViewById(R.id.addressValue);
        phoneNumValue = (EditText) findViewById(R.id.phoneNumValue);
        emailValue = (EditText) findViewById(R.id.emailValue);
        passwordValue = (EditText) findViewById(R.id.passwordValue);
        occupationValue = (EditText) findViewById(R.id.occupationValue);

        doneSignUpButton = (MaterialButton) findViewById(R.id.doneSignUpButton);
        confirmSignUpButton = (MaterialButton) findViewById(R.id.confirmSignUpButton);
        roleDropDown = (Spinner) findViewById(R.id.roleDropDown);
        genderDropDown = (Spinner) findViewById(R.id.genderDropDown);
        mainServicesDropDown = (Spinner) findViewById(R.id.mainServicesDropDown);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);
        progressBarBackground = findViewById(R.id.progressBarBackground);

        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Categories");

        final ArrayAdapter<String> serviceNameArrayAdapter = new ArrayAdapter<>(SignUp.this, R.layout.spinner_item);
        serviceNameArrayAdapter.add("Select Main Service");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String serviceName = childSnapshot.child("serviceName").getValue(String.class);

                    if (serviceName != null) {
                        serviceNameArrayAdapter.add(serviceName);
                    }
                }

                mainServicesDropDown.setAdapter(serviceNameArrayAdapter);

                mainServicesDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (parent.getTag() != null) {
                                parent.setSelection((Integer) parent.getTag());
                            } else {
                                Toast.makeText(getApplicationContext(), "Select a valid Main Service", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            parent.setTag(position);
                            String selectedItemText = (String) parent.getItemAtPosition(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the database read
            }
        });

        String[] roleItems = new String[]{"Select Role","User", "Service Provider"};
        String[] genderItems = new String[]{"Select Gender","Male", "Female"};

        final List<String> roleList = new ArrayList<>(Arrays.asList(roleItems));
        final List<String> genderList = new ArrayList<>(Arrays.asList(genderItems));

        final ArrayAdapter<String> roleSpinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, roleList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        final ArrayAdapter<String> genderSpinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, genderItems){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                { return false; }
                else
                { return true; }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        roleSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        genderSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        roleDropDown.setAdapter(roleSpinnerArrayAdapter);
        genderDropDown.setAdapter(genderSpinnerArrayAdapter);

        roleDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    Toast.makeText(getApplicationContext(), "Selected Role: " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    Toast.makeText(getApplicationContext(), "Selected Gender: " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doneSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String role = roleDropDown.getSelectedItem().toString();

                if (role == "Service Provider") {
                    serviceProviderInputWrapper.setVisibility(View.VISIBLE);
                } else {
                    signUpEvent();
                }
            }

        });

        confirmSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceProviderInputWrapper.setVisibility(View.GONE);
                signUpEvent();
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
        String gender = genderDropDown.getSelectedItem().toString();
        String phoneNum = phoneNumValue.getText().toString().trim();
        String email = emailValue.getText().toString().trim();
        String password = passwordValue.getText().toString().trim();
        String role = roleDropDown.getSelectedItem().toString();
        String occupation = occupationValue.getText().toString().trim();
        String mainService = mainServicesDropDown.getSelectedItem().toString();
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

        if (gender.matches("Select Gender")){
            ((TextView)genderDropDown.getSelectedView()).setError("Gender Required");
            genderDropDown.requestFocus();
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

        if (role.matches("Select Role")){
            ((TextView)roleDropDown.getSelectedView()).setError("Role Required");
            genderDropDown.requestFocus();
            return;
        }

        if (role == "Service Provider"){
            if (TextUtils.isEmpty(occupation)){
                occupationValue.setError("Occupation Required");
                return;
            }

            if (mainService == "Select Main Service"){
                ((TextView)mainServicesDropDown.getSelectedView()).setError("Select valid main service");
                return;
            }
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Account account = new Account(accID, name, address, gender, phoneNum, email, password, role, occupation, starRating,  mainService, earnings,  countBookings);

                    if (account.mainService == "Select Main Service"){
                        account.mainService = "User";
                    }

                    FirebaseDatabase.getInstance("https://serbisyofinder-f09d8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Account")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        progressBarText.setVisibility(View.VISIBLE);
                                        progressBarBackground.setVisibility(View.VISIBLE);

                                        startActivity(new Intent(SignUp.this, SuccessSignUp.class));
                                    } else
                                        Toast.makeText(SignUp.this, "Sign-Up Failed!"
                                                + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }else
                    Toast.makeText(SignUp.this, "Sign-Up Failed! - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                progressBarText.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
            }
        });
    }

}