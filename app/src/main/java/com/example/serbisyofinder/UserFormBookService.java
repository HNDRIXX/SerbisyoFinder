package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import androidx.recyclerview.widget.RecyclerView;

//import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class UserFormBookService extends AppCompatActivity implements ViewMap.LocationDataListener  {

    TextView providerIDText, providerNameText, providerOccupationText, locationTextValue, disabledDaysList;

    private MaterialButton datePickerButton;
    private Calendar currentDate;

    FirebaseUser account;
    DatabaseReference reference, databaseReference;

    String serviceID, clientID, clientPhoneNum, flag;
    String clientLatitude, clientLongitude;
    String clientName, currAddress, formattedDate, accIDValue, providerPhoneNum;
    String accID, providerName, providerOccupation, mainService, subService, address;

    ArrayList<Calendar> disabledDays = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> schedules = new ArrayList<>();

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

        Fragment fragment = new ViewMap();

        MaterialButton confirmButton = (MaterialButton) findViewById(R.id.confirmBookButton);
        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        disabledDaysList = (TextView) findViewById(R.id.disabledDaysList);

        providerIDText = (TextView) findViewById(R.id.providerID);
        providerNameText = (TextView) findViewById(R.id.providerName);
        providerOccupationText = (TextView) findViewById(R.id.providerOccupation);
        locationTextValue = (TextView) findViewById(R.id.locationValueText);

        serviceID = getIntent().getStringExtra("serviceID");
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

//        recyclerView = (RecyclerView) findViewById(R.id.providerSchedList);
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(UserFormBookService.this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(horizontalLayoutManager);
//
//        FirebaseRecyclerOptions<Schedule> options =
//                new FirebaseRecyclerOptions.Builder<Schedule>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Provider Schedule").orderByChild("accID").equalTo(accID), Schedule.class)
//                        .build();
//
//        adapterSchedule = new AdapterSchedule(options);
//        recyclerView.setAdapter(adapterSchedule);

        account = FirebaseAuth.getInstance().getCurrentUser();
        clientID = account.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Provider Schedule");
        databaseReference.orderByChild("accID").equalTo(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder stringBuilder = new StringBuilder();

                schedules.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String providerSchedule = childSnapshot.child("providerSchedule").getValue(String.class);
                    String providerMultiSchedule = childSnapshot.child("providerMultiSchedule").getValue(String.class);

                    String flagValue = childSnapshot.child("flag").getValue(String.class);

                    if (flagValue.equals("1") || flagValue.equals("2")) {
                        if (providerMultiSchedule != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(providerMultiSchedule);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String dateString = jsonArray.getString(i);

                                    stringBuilder.append("‣ " + dateString).append("\n");
                                }

                                disabledDaysList.setText(stringBuilder.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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
                if (datePickerButton.getText().toString().trim().matches("PICK DATE")){
                    datePickerButton.setError("Empty");
                    Toast.makeText(UserFormBookService.this, "Schedule Date Required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (clientLatitude.matches("") || clientLatitude.isEmpty()){
                    Toast.makeText(UserFormBookService.this, "Please wait to get your location, MAKE SURE TO OPEN YOUR LOCATION!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (clientLongitude.matches("") || clientLongitude.isEmpty()){
                    Toast.makeText(UserFormBookService.this, "Please wait to get your location, MAKE SURE TO OPEN YOUR LOCATION!", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONArray jsonArray = new JSONArray(dates);
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
                dataToSet.put("price", "");
                dataToSet.put("clientName", clientName);
                dataToSet.put("clientPhoneNum", clientPhoneNum);
                dataToSet.put("providerPhoneNum", providerPhoneNum);
                dataToSet.put("providerName", providerName);
                dataToSet.put("providerSchedule", formattedDate);
                dataToSet.put("providerMultiSchedule", jsonArray.toString());
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
        Calendar now = Calendar.getInstance();

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 2);

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        if (selectedDate.after(minDate) && selectedDate.before(maxDate)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
                            formattedDate = sdf.format(selectedDate.getTime());

                            if (!dates.contains(formattedDate)) {
                                Toast.makeText(getApplicationContext(), "Selected date: " + formattedDate, Toast.LENGTH_SHORT).show();

                                datePickerButton.setText(formattedDate);
                                dates.add(formattedDate);
                            } else {
                                Toast.makeText(getApplicationContext(), "Selected date already exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select a date within the next 3 months", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.setMinDate(minDate);
        dpd.setMaxDate(maxDate);

        fetchAndDisableDatesFromFirebase(dpd);

        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private void fetchAndDisableDatesFromFirebase(final DatePickerDialog dpd) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Provider Schedule");
        databaseReference.orderByChild("accID").equalTo(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String providerSchedule = childSnapshot.child("providerSchedule").getValue(String.class);
                    String providerMultiSchedule = childSnapshot.child("providerMultiSchedule").getValue(String.class);

                    String flagValue = childSnapshot.child("flag").getValue(String.class);

                    if (flagValue.equals("1") || flagValue.equals("2")) {
                        Calendar disabledDate = convertStringToCalendar(providerSchedule);

                        if (disabledDate != null) {
                            disabledDays.add(disabledDate);
                        }

                        if (providerMultiSchedule != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(providerMultiSchedule);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String dateString = jsonArray.getString(i);
                                    disabledDate = convertStringToCalendar(dateString);

                                    if (disabledDate != null ) {
                                        disabledDays.add(disabledDate);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                dpd.setDisabledDays(disabledDays.toArray(new Calendar[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



    private Calendar convertStringToCalendar(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            Date date = sdf.parse(dateString);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String calendarToString(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
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