package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserFormBookService extends AppCompatActivity implements ViewMap.LocationDataListener  {

    TextView providerIDText, providerNameText, providerOccupationText, locationTextValue;

    private MaterialButton datePickerButton;
    private Calendar currentDate;

    FirebaseUser account;
    DatabaseReference reference, databaseReference;

    String clientID, clientPhoneNum;
    String clientLatitude = "clientLatitude";
    String clientLongitude = "clientLongitude";
    String clientName, currAddress, accIDValue, providerPhoneNum;
    String formattedDate = "formattedDate";
    String accID, providerName, providerOccupation, mainService, subService, address;

    RecyclerView recyclerView;
    AdapterSchedule adapterSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_book_service);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserFormBookService.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {}

        if (!network_enabled) {
            new AlertDialog.Builder(this)
                    .setMessage("Location is not enabled!")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            finish();
                            startActivity(getIntent());
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }

        Fragment fragment = new ViewMap();

        MaterialButton confirmButton = (MaterialButton) findViewById(R.id.confirmBookButton);
        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        providerIDText = (TextView) findViewById(R.id.providerID);
        providerNameText = (TextView) findViewById(R.id.providerName);
        providerOccupationText = (TextView) findViewById(R.id.providerOccupation);
        locationTextValue = (TextView) findViewById(R.id.locationValueText);

        accID = getIntent().getStringExtra("accID");
        providerPhoneNum = getIntent().getStringExtra("providerPhoneNum");
        providerName = getIntent().getStringExtra("providerName");
        mainService = getIntent().getStringExtra("serviceName");
        subService = getIntent().getStringExtra("subService");
        providerOccupation = getIntent().getStringExtra("providerOccupation");
        address = getIntent().getStringExtra("address");

        datePickerButton = (MaterialButton) findViewById(R.id.datePickerButton);

        currentDate = Calendar.getInstance();

        providerIDText.setText(accID);
        providerNameText.setText(providerName);
        providerOccupationText.setText(providerOccupation);
        locationTextValue.setText(address);

        ViewMap viewMapFragment = new ViewMap();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, viewMapFragment)
                .commit();


        recyclerView = (RecyclerView) findViewById(R.id.providerSchedList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(UserFormBookService.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        FirebaseRecyclerOptions<Schedule> options =
                new FirebaseRecyclerOptions.Builder<Schedule>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Provider Schedule").orderByChild("accID").equalTo(accID), Schedule.class)
                        .build();

        adapterSchedule = new AdapterSchedule(options);
        recyclerView.setAdapter(adapterSchedule);

        account = FirebaseAuth.getInstance().getCurrentUser();
        clientID = account.getUid();

        FirebaseDatabase.getInstance().getReference("Account").child(clientID).child("phoneNum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    clientPhoneNum = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseDatabase.getInstance().getReference("Account").child(clientID).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    clientName = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseDatabase.getInstance().getReference("Account").child(clientID).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currAddress = dataSnapshot.getValue(String.class);

                    locationTextValue.setText(currAddress);
                } else {
                    Toast.makeText(getApplicationContext(), "No Address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Provider Schedule");
                databaseReference.orderByChild("accID").equalTo(accID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isExist = false;

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String providerSchedule = childSnapshot.child("providerSchedule").getValue(String.class);
                            if (formattedDate.equals(providerSchedule)) {
                                isExist = true;
                            }
                        }

                        if (isExist) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Date is unavailable. Please select another date.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            return;
                        } else {
                            if (datePickerButton.getText().toString().trim().equals("PICK DATE")){
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Booking Date is Required!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return;
                            }

                            if (clientLatitude.equals("clientLatitude") || clientLatitude.isEmpty() || clientLongitude.equals("clientLongitude")|| clientLongitude.isEmpty()){
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please wait to get your location, Make sure to open your location.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return;
                            }

                            Random random = new Random();
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < 8; i++) {
                                stringBuilder.append(random.nextInt(10));
                            }

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            String path = "Provider Schedule";
                            DatabaseReference databaseReference = database.getReference(path);

                            String key = databaseReference.push().getKey();

                            DatabaseReference reference = databaseReference.child(key);


                            Map<String, Object> dataToSet = new HashMap<>();
                            dataToSet.put("accID", accID);
                            dataToSet.put("scheduleID", stringBuilder.toString().trim());
                            dataToSet.put("clientID", clientID);
                            dataToSet.put("mainService", mainService);
                            dataToSet.put("subService", subService);
                            dataToSet.put("clientAddress", currAddress);
                            dataToSet.put("price", "Not Set");
                            dataToSet.put("clientName", clientName);
                            dataToSet.put("clientPhoneNum", clientPhoneNum);
                            dataToSet.put("providerPhoneNum", providerPhoneNum);
                            dataToSet.put("providerName", providerName);
                            dataToSet.put("providerSchedule", formattedDate);
                            dataToSet.put("clientLatitude", clientLatitude);
                            dataToSet.put("clientLongitude", clientLongitude);
                            dataToSet.put("status", "Waiting for Confirmation");
                            dataToSet.put("rating", "0");
                            dataToSet.put("review", "No Review");
                            dataToSet.put("flag", "1");

                            reference.updateChildren(dataToSet).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        //                            DatabaseReference myRef = database.getReference("Messages");
                                        //                            DatabaseReference newRef = myRef.child(myRef.push().getKey());
                                        //                            newRef.child("providerID").setValue(accID);
                                        //                            newRef.child("clientID").setValue(clientID);

                                        startActivity(new Intent(UserFormBookService.this, SuccessBookService.class));
                                    } else {
                                        // Data setting failed, show a Toast message
                                        Toast.makeText(UserFormBookService.this, "Data set failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(backButton.getContext())
                        .setTitle("Cancel Form Booking")
                        .setMessage("Are you sure?")
                        .setPositiveButton(Html.fromHtml("<font color='#FF7F09'>Yes</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(UserFormBookService.this, UserBookServices.class));
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

    public void onLocationDataReceived(double latitude, double longitude) {
        clientLatitude = String.valueOf(latitude);
        clientLongitude = String.valueOf(longitude);
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                if (!selectedDate.before(currentDate)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
                    formattedDate = sdf.format(selectedDate.getTime());

                    Toast.makeText(getApplicationContext(), "Selected date: " + formattedDate, Toast.LENGTH_SHORT).show();

                    datePickerButton.setText(formattedDate);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a future date", Toast.LENGTH_SHORT).show();
                }
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterSchedule.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterSchedule.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Form Booking")
                .setMessage("Are you sure?")
                .setPositiveButton(Html.fromHtml("<font color='#FF7F09'>Yes</font>"), new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(UserFormBookService.this, UserBookServices.class));
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

}