package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserBookingsPanel extends AppCompatActivity {
    String key, serviceID, providerID, clientID, flag;
    String scheduleID, mainService, subService, clientName, providerName, price, status, providerPhoneNum;
    TextView scheduleIDValue, providerPhoneNumValue, mainServiceValue, subServiceValue, providerNameValue, statusValue, subStatusValue;
    EditText priceValue;

    ImageView waitingIcon, confirmedIcon, completedIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings_panel);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserBookingsPanel.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        waitingIcon = (ImageView) findViewById(R.id.waitingIcon);
        confirmedIcon = (ImageView) findViewById(R.id.confirmedIcon);
        completedIcon = (ImageView) findViewById(R.id.completedIcon);

        MaterialButton cancelButton = (MaterialButton) findViewById(R.id.cancelButton);
        MaterialButton setPriceButton = (MaterialButton) findViewById(R.id.setPriceButton);
        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton messageButton = (MaterialButton) findViewById(R.id.messageButton);
        MaterialButton rateButton = (MaterialButton) findViewById(R.id.rateButton);

        key = getIntent().getStringExtra("key");
        flag = getIntent().getStringExtra("flag");
        scheduleID = getIntent().getStringExtra("scheduleID");
        providerID = getIntent().getStringExtra("providerID");
        providerPhoneNum = getIntent().getStringExtra("providerPhoneNum");
        clientID = getIntent().getStringExtra("clientID");
        mainService = getIntent().getStringExtra("mainService");
        subService = getIntent().getStringExtra("subService");
        clientName = getIntent().getStringExtra("clientName");
        providerName = getIntent().getStringExtra("providerName");
        price = getIntent().getStringExtra("price");
        status = getIntent().getStringExtra("status");

        scheduleIDValue = (TextView) findViewById(R.id.scheduleIDValue);
        mainServiceValue = (TextView) findViewById(R.id.mainServiceValue);
        subServiceValue = (TextView) findViewById(R.id.subServiceValue);
        providerNameValue = (TextView) findViewById(R.id.providerNameValue);
        priceValue = (EditText) findViewById(R.id.priceValue);
        statusValue = (TextView) findViewById(R.id.statusValue);
        providerPhoneNumValue = (TextView) findViewById(R.id.providerPhoneNumValue);
        subStatusValue = (TextView) findViewById(R.id.subStatusValue);

        scheduleIDValue.setText(scheduleID);
        mainServiceValue.setText(mainService);
        subServiceValue.setText(subService);
        providerNameValue.setText(providerName);
        providerPhoneNumValue.setText(providerPhoneNum);

        setPriceButton.setVisibility(View.GONE);
        priceValue.setFocusable(false);
        priceValue.setEnabled(false);
        priceValue.setCursorVisible(false);
        priceValue.setKeyListener(null);

        if (price.isEmpty()) {
            priceValue.setText("Not Set");
        } else {
            priceValue.setText("₱ " + price);
        }

        statusValue.setText(status);

        if ("Waiting for Confirmation".equals(status)) {
           waitingIcon.setVisibility(View.VISIBLE);
           subStatusValue.setText("Your booking is received by the service provider. Please wait for confirmation.");
           cancelButton.setVisibility(View.VISIBLE);
        } else if ("Service Confirmed".equals(status)) {
            confirmedIcon.setVisibility(View.VISIBLE);
            messageButton.setVisibility(View.VISIBLE);
            subStatusValue.setText("This booking is confirmed! The service provider will do the job for your needs.");
        } else if ("Completed".equals(status)) {
            completedIcon.setVisibility(View.VISIBLE);
            rateButton.setVisibility(View.VISIBLE);
            subStatusValue.setText("This booking is completed!");
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Provider Schedule");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserBookingsPanel.this, UserBookings.class));
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserBookingsPanel.this, UserRate.class);
                intent.putExtra("scheduleID", scheduleID);
                intent.putExtra("providerID", providerID);
                startActivity(intent);
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserBookingsPanel.this, Message.class);
                intent.putExtra("scheduleID", scheduleID);
                intent.putExtra("clientName", clientName);
                intent.putExtra("clientID", clientID);
                intent.putExtra("providerID", providerID);
                intent.putExtra("providerName", providerName);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("flag", "0");
                updates.put("status", "Cancelled");

                databaseReference.child(key).updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            Toast.makeText(UserBookingsPanel.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserBookingsPanel.this, UserBookings.class));
                        }
                    }
                });
            }
        });

        setPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child(key).child("price").setValue(priceValue.getText().toString().trim(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            Toast.makeText(UserBookingsPanel.this, "Set price successfully!", Toast.LENGTH_SHORT).show();
                        } else {

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(UserBookingsPanel.this, UserBookings.class));
    }
}