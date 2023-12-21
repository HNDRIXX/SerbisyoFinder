package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;

public class UserRate extends AppCompatActivity {
    String ratingValue, scheduleID, providerID;
    DatabaseReference reference;

    double averageRate = 0.0;
    double currRateBar = 0.0;
    int nonZeroRateCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserRate.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton submitRateButton = (MaterialButton) findViewById(R.id.submitRateButton);
        EditText reviewTextValue = (EditText) findViewById(R.id.reviewTextValue);

        scheduleID = getIntent().getStringExtra("scheduleID");
        providerID = getIntent().getStringExtra("providerID");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = String.valueOf(rating);
                currRateBar = rating;
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Provider Schedule");

        reference.orderByChild("accID").equalTo(providerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalRate = 0.0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    if (schedule != null && schedule.rating != null && !schedule.rating.equals("0")) {
                        try {
                            double rate = Double.parseDouble(schedule.rating);
                            totalRate += rate;

                            nonZeroRateCount++;
                        } catch (NumberFormatException e) {
                        }
                    }
                }

                averageRate = totalRate;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserRate.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        submitRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DecimalFormat single = new DecimalFormat("#");
                DecimalFormat decimalFormat = new DecimalFormat("#.#");

                double nowNonZero = nonZeroRateCount + 1;

                double sum = averageRate + currRateBar;
                double result = sum / nowNonZero;
                double nowRating = result;

                String formattedRating = decimalFormat.format(nowRating);

//                Toast.makeText(UserRate.this, String.valueOf(averageRate), Toast.LENGTH_SHORT).show();
                if (reviewTextValue.getText().toString().trim().isEmpty()){
                    Toast.makeText(UserRate.this, "Please complete your inputs before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (reviewTextValue.getText().toString().trim().length() < 10) {
                    Toast.makeText(UserRate.this, "Your review is short, Add more to it.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Provider Schedule");

                databaseReference.orderByChild("scheduleID").equalTo(scheduleID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference childReference = childSnapshot.getRef();

                            HashMap<String, Object> updateData = new HashMap<>();
                            updateData.put("flag", "0");
                            updateData.put("rating", ratingValue);
                            updateData.put("review", reviewTextValue.getText().toString().trim());

                            childReference.updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference();
                                        databaseReferences.child("Account").child(providerID).child("starRating").setValue(formattedRating);

                                        startActivity(new Intent(UserRate.this, SuccessRate.class));
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
                startActivity(new Intent(UserRate.this, UserBookings.class));
            }
        });
    }
}