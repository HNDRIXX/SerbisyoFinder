package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdminServiceProvider extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterProviders adapterProviders;

    String accID;
    FirebaseUser account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_provider);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(AdminServiceProvider.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        RelativeLayout noResultDisplay = (RelativeLayout) findViewById(R.id.noResultDisplay);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.providersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        account = FirebaseAuth.getInstance().getCurrentUser();
        accID = account.getUid();

        FirebaseRecyclerOptions<Account> options =
                new FirebaseRecyclerOptions.Builder<Account>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Account").orderByChild("role").equalTo("Service Provider"), Account.class)
                        .build();

        adapterProviders = new AdapterProviders(options);
        recyclerView.setAdapter(adapterProviders);

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

                    if (visibleItemCount <= 0) {
                        progressBar.setVisibility(View.GONE);
                        noResultDisplay.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminServiceProvider.this, AdminHome.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterProviders.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterProviders.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(AdminServiceProvider.this, AdminHome.class));
    }
}