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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSelectServiceProvider extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterServiceProvider adapterServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select_service_provider);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserSelectServiceProvider.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        TextView noResultText = (TextView) findViewById(R.id.noResultText);

        String serviceName = getIntent().getStringExtra("serviceName");
        String subService = getIntent().getStringExtra("subService");

        recyclerView = (RecyclerView) findViewById(R.id.serviceProviderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Account> options =
                new FirebaseRecyclerOptions.Builder<Account>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Account").orderByChild("mainService").equalTo(serviceName), Account.class)
                        .build();

        adapterServiceProvider = new AdapterServiceProvider(options, serviceName, subService);
        recyclerView.setAdapter(adapterServiceProvider);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSelectServiceProvider.this, UserBookServices.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterServiceProvider.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterServiceProvider.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(UserSelectServiceProvider.this, UserBookServices.class));
    }
}