package com.example.serbisyofinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser account;
    DatabaseReference reference;
    String accountID;

    RelativeLayout mainLayout;

    EditText emailValue;
    TextInputEditText passwordValue;

    ProgressBar progressBar;
    ImageView progressBarBackground;
    TextView progressBarText;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

//        FirebaseDatabase databases = FirebaseDatabase.getInstance();
//
//        String existingPath = "Provider Schedule";
//        DatabaseReference existingRef = databases.getReference(existingPath);
//
//        String newKey = existingRef.push().getKey();
//
//        DatabaseReference newRef = existingRef.child(newKey);
//        newRef.child("serviceID").setValue("45236");

//        FirebaseDatabase databases = FirebaseDatabase.getInstance();
//        String existingPath = "Provider Schedule"; // Replace with the existing path
//        DatabaseReference existingRef = databases.getReference(existingPath);
//        String newKey = existingRef.push().getKey();
//        DatabaseReference newRef = existingRef.child(newKey);
//        newRef.child("accID").setValue("32356");
//        newRef.child("providerSchedule").setValue("October 17, 2023");

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(MainActivity.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton signUpButton = (MaterialButton) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        MaterialButton signInButton = (MaterialButton) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        emailValue = (EditText) findViewById(R.id.email);
        passwordValue = (TextInputEditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        progressBarText = findViewById(R.id.progressBarText);

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInEvent();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    public void signInEvent() {
        String email = emailValue.getText().toString().trim();
        String password = passwordValue.getText().toString().trim();

//        String email = "tom@gmail.com";
//        String password = "123456";

        if (TextUtils.isEmpty(email)){
            emailValue.setError("Email Required!");
            return;
        }

        if (TextUtils.isEmpty(password)){
            passwordValue.setError("Password Required!");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        //hide keyboard when user press the login button.
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    account = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance("https://serbisyofinder-f09d8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Account");
                    accountID = account.getUid();

                    reference.child(accountID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Account accountInfo = snapshot.getValue(Account.class);

                            String pass = accountInfo.password;

                            String mail = accountInfo.email;
                            String role = accountInfo.role;

                            if (mail.equals(email) && pass.equals(password) && role.equals("Admin")){

                                progressBar.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);

                                startActivity(new Intent(MainActivity.this, AdminHome.class));

                            } else if (mail.equals(email) && pass.equals(password) && role.equals("Super Admin")) {

                                progressBar.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);

                                startActivity(new Intent(MainActivity.this, SuperAdminHome.class));
                            }else if (mail.equals(email) && pass.equals(password) && role.equals("User")) {

                                progressBar.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);

                                startActivity(new Intent(MainActivity.this, UserHome.class));
                            } else if (mail.equals(email) && pass.equals(password) && role.equals("Service Provider")) {

                                progressBar.setVisibility(View.VISIBLE);
                                progressBarBackground.setVisibility(View.VISIBLE);
                                progressBarText.setVisibility(View.VISIBLE);

                                startActivity(new Intent(MainActivity.this, ProviderHome.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                        }
                    });
                }else {
                    progressBar.setVisibility(View.GONE);
                    progressBarBackground.setVisibility(View.GONE);
                    progressBarText.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(MainActivity.this, "Sign-In Failed! - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
        System.exit(0);
    }

}

