package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderPendingBookings extends AppCompatActivity {
    FirebaseUser account;
    DatabaseReference reference;
    String accID;

    RecyclerView recyclerView;
    AdapterPendingBookings adapterPendingBookings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_pending_bookings);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(ProviderPendingBookings.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        RelativeLayout noResultDisplay = (RelativeLayout) findViewById(R.id.noResultDisplay);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://serbisyofinder-f09d8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Account");
        accID = account.getUid();

        recyclerView = (RecyclerView) findViewById(R.id.pendingBookingsList);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);

        FirebaseRecyclerOptions<Schedule> options =
                new FirebaseRecyclerOptions.Builder<Schedule>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Provider Schedule").orderByChild("accID").equalTo(accID), Schedule.class)
                        .build();

        adapterPendingBookings = new AdapterPendingBookings(options);
        recyclerView.setAdapter(adapterPendingBookings);

        int itemCount = adapterPendingBookings.getItemCount();

       if (itemCount == 0){
           progressBar.setVisibility(View.GONE);
           noResultDisplay.setVisibility(View.VISIBLE);
       }

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if (layoutManager != null) {
            layoutManager.scrollToPosition(0);
            layoutManager.setSmoothScrollbarEnabled(true);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    int visibleItemCount = 0;

                    for (int i = 0; i < layoutManager.getChildCount(); i++) {
                        View child = layoutManager.getChildAt(i);
                        int width = child.getWidth();
                        int height = child.getHeight();
                        if (width > 0 && height > 0) {
                            visibleItemCount++;
                        }
                    }

                    if (visibleItemCount <= 0 || layoutManager.getChildCount() == 0){
                        progressBar.setVisibility(View.GONE);
                        noResultDisplay.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ProviderPendingBookings.this, ProviderHome.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterPendingBookings.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterPendingBookings.stopListening();
    }

    public void onBackPressed()
    {
        startActivity(new Intent(ProviderPendingBookings.this, ProviderHome.class));
    }
}