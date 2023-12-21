package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProviderConfirmedPanel extends AppCompatActivity {
    String providerID, clientID;
    String scheduleID, mainService, subService, clientAddress, clientName, providerName, price, status, clientLatitude, clientLongitude;
    TextView scheduleIDValue, mainServiceValue, subServiceValue, clientNameValue, priceValue, locationValueText, totalEarningsValue, totalBookingsValue;

    FirebaseUser account;
    DatabaseReference reference;
    String accID, totalEarnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_confirmed_panel);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(ProviderConfirmedPanel.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton markCompleteButton = (MaterialButton) findViewById(R.id.markCompleteButton);
        MaterialButton messageButton = (MaterialButton) findViewById(R.id.messageButton);


        scheduleID = getIntent().getStringExtra("scheduleID");
        providerID = getIntent().getStringExtra("providerID");
        clientAddress = getIntent().getStringExtra("clientAddress");
        clientID = getIntent().getStringExtra("clientID");
        mainService = getIntent().getStringExtra("mainService");
        subService = getIntent().getStringExtra("subService");
        clientName = getIntent().getStringExtra("clientName");
        providerName = getIntent().getStringExtra("providerName");
        clientLatitude = getIntent().getStringExtra("clientLatitude");
        clientLongitude = getIntent().getStringExtra("clientLongitude");
        price = getIntent().getStringExtra("price");
        status = getIntent().getStringExtra("status");

        mainServiceValue = (TextView) findViewById(R.id.mainServiceValue);
        subServiceValue = (TextView) findViewById(R.id.subServiceValue);
        clientNameValue = (TextView) findViewById(R.id.clientNameValue);
        priceValue = (TextView) findViewById(R.id.priceValue);
        locationValueText = (TextView) findViewById(R.id.locationValueText);
        totalEarningsValue = (TextView) findViewById(R.id.totalEarningsValue);
        totalBookingsValue = (TextView) findViewById(R.id.totalBookingsValue);

        mainServiceValue.setText(mainService);
        subServiceValue.setText(subService);
        clientNameValue.setText(clientName);
        priceValue.setText(price);
        locationValueText.setText(clientAddress);

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Account");
        accID = account.getUid();
        reference.child(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account userProfile = snapshot.getValue(Account.class);

                totalEarningsValue.setText(userProfile.earnings);
                totalBookingsValue.setText(userProfile.countBookings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String latitudeValueMap = clientLatitude;
        String longitudeValueMap = clientLongitude;
        double latitude = Double.parseDouble(latitudeValueMap);
        double longitude = Double.parseDouble(longitudeValueMap);

        Fragment fragment = ProviderMap.newInstance(latitude, longitude);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderConfirmedPanel.this, Message.class);
                intent.putExtra("scheduleID", scheduleID);
                intent.putExtra("clientName", clientName);
                intent.putExtra("clientID", clientID);
                intent.putExtra("providerID", providerID);
                intent.putExtra("providerName", providerName);
                startActivity(intent);
            }
        });

        markCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Provider Schedule");

                databaseReference.orderByChild("scheduleID").equalTo(scheduleID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference childReference = childSnapshot.getRef();

                            HashMap<String, Object> updateData = new HashMap<>();
                            updateData.put("flag", "3");
                            updateData.put("status", "Completed");

                            childReference.updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase  database = FirebaseDatabase.getInstance();
                                        DatabaseReference mDatabaseRef = database.getReference("Account");

                                        mDatabaseRef.child(accID).child("earnings").setValue(String.valueOf(Integer.parseInt(totalEarningsValue.getText().toString().trim()) + Integer.parseInt(price)));
                                        mDatabaseRef.child(accID).child("countBookings").setValue(String.valueOf(Integer.parseInt(totalBookingsValue.getText().toString().trim()) + 1));
                                        startActivity(new Intent(ProviderConfirmedPanel.this, SuccessConfirmedBooking.class));
                                    } else {
                                        Exception e = task.getException();
                                        if (e != null) {
                                            String errorMessage = e.getMessage();
                                            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProviderConfirmedPanel.this, ProviderConfirmedBookings.class));
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(ProviderConfirmedPanel.this, ProviderConfirmedBookings.class));
    }
}