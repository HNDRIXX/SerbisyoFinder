package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    FirebaseUser account;
    DatabaseReference reference;
    String accID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserProfile.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        TextView addressValue = (TextView) findViewById(R.id.addressValue);
        TextView genderValue = (TextView) findViewById(R.id.genderValue);
        TextView emailValue = (TextView) findViewById(R.id.emailValue);
        TextView phoneNumValue = (TextView) findViewById(R.id.phoneNumValue);
        TextView nameValue = (TextView) findViewById(R.id.nameValue);
        TextView roleValue = (TextView) findViewById(R.id.roleValue);

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Account");
        accID = account.getUid();
        reference.child(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account userProfile = snapshot.getValue(Account.class);

                nameValue.setText(userProfile.name);
                addressValue.setText(userProfile.address);
                genderValue.setText(userProfile.gender);
                emailValue.setText(userProfile.email);
                phoneNumValue.setText(userProfile.phoneNum);
                roleValue.setText(userProfile.role);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, UserHome.class));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(UserProfile.this, UserHome.class));
    }
}