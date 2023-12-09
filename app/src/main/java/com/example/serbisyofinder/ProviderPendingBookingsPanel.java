package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProviderPendingBookingsPanel extends AppCompatActivity {
    String providerID, clientID;
    String scheduleID, mainService, subService, clientAddress, clientName, providerName, price, status, clientLatitude, clientLongitude;
    TextView scheduleIDValue, mainServiceValue, subServiceValue, clientNameValue, locationValueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_bookings_panel);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(ProviderPendingBookingsPanel.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton confirmThisBookButton = (MaterialButton) findViewById(R.id.confirmThisBookButton);
        EditText setPriceValue = (EditText) findViewById(R.id.setPriceValue);

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

        scheduleIDValue = (TextView) findViewById(R.id.scheduleIDValue);
        mainServiceValue = (TextView) findViewById(R.id.mainServiceValue);
        subServiceValue = (TextView) findViewById(R.id.subServiceValue);
        clientNameValue = (TextView) findViewById(R.id.clientNameValue);
        locationValueText = (TextView) findViewById(R.id.locationValueText);

        scheduleIDValue.setText(scheduleID);
        mainServiceValue.setText(mainService);
        subServiceValue.setText(subService);
        clientNameValue.setText(clientName);
        locationValueText.setText(clientAddress);

        String latitudeValueMap = clientLatitude;
        String longitudeValueMap = clientLongitude;

        double latitude = Double.parseDouble(latitudeValueMap);
        double longitude = Double.parseDouble(longitudeValueMap);

        Fragment fragment = ProviderMap.newInstance(latitude, longitude);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutPending, fragment)
                .commit();

        confirmThisBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Provider Schedule");

                databaseReference.orderByChild("scheduleID").equalTo(scheduleID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (setPriceValue.getText().toString().trim().matches("")){
                            setPriceValue.setError("Price is not set.");
                            return;
                        }

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            // Get the reference for the specific child node
                            DatabaseReference childReference = childSnapshot.getRef();

                            HashMap<String, Object> updateData = new HashMap<>();
                            updateData.put("flag", "2");
                            updateData.put("status", "Service Confirmed");
                            updateData.put("price", setPriceValue.getText().toString().trim());

                            childReference.updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(ProviderPendingBookingsPanel.this, SuccessConfirmedPending.class));
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
            public void onClick(View view){
                startActivity(new Intent(ProviderPendingBookingsPanel.this, ProviderPendingBookings.class));
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(ProviderPendingBookingsPanel.this, ProviderPendingBookings.class));
    }
}