package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderHome extends AppCompatActivity {

    FirebaseUser account;
    DatabaseReference reference;
    String accID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(ProviderHome.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton pendingBookingsButton = (MaterialButton) findViewById(R.id.providerPendingBookingsButton);
        MaterialButton confirmedBookingsButton = (MaterialButton) findViewById(R.id.providerConfirmedBookingsButton);
        MaterialButton bookingRecordsButton = (MaterialButton) findViewById(R.id.providerBookingsButton);
        MaterialButton profileButton = (MaterialButton) findViewById(R.id.providerProfileButton);
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);

        TextView totalEarningsValue = (TextView) findViewById(R.id.totalEarningsValue);
        TextView totalBookingsValue = (TextView) findViewById(R.id.totalBookingsValue);
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Account");
        accID = account.getUid();

        reference.child(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account userProfile = snapshot.getValue(Account.class);

                if (userProfile != null) {
                    String name = userProfile.name;
                    String arr[] = name.split(" ", 2);

                    String currName = arr[0];

                    welcomeText.setText("Welcome, " + currName + "!");
                }

                totalBookingsValue.setText(userProfile.countBookings);
                totalEarningsValue.setText(userProfile.earnings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProviderHome.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        reference.child(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account userProfile = snapshot.getValue(Account.class);

                totalEarningsValue.setText("₱ " + userProfile.earnings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProviderHome.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        pendingBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProviderHome.this, ProviderPendingBookings.class));
            }
        });

        confirmedBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProviderHome.this, ProviderConfirmedBookings.class));
            }
        });

        bookingRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProviderHome.this, ProviderBookingRecords.class));
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProviderHome.this, ProviderProfile.class));
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(signOutButton.getContext())
                        .setTitle("Confirm Sign-Out")
                        .setMessage("Are you sure?")
                        .setPositiveButton(Html.fromHtml("<font color='#FF7F09'>Yes</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProviderHome.this, MainActivity.class);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#FF7F09'>No</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.icon_warning)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Sign-Out")
                .setMessage("Are you sure?")
                .setPositiveButton(Html.fromHtml("<font color='#FF7F09'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProviderHome.this, MainActivity.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#FF7F09'>No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.icon_warning)
                .show();
    }
}