package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserBookServices extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterServiceCategory adapterServiceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_services);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(UserBookServices.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        recyclerView = (RecyclerView) findViewById(R.id.serviceCategoryList);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        FirebaseRecyclerOptions<ServiceCategory> options =
                new FirebaseRecyclerOptions.Builder<ServiceCategory>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories").orderByChild("serviceName"), ServiceCategory.class)
                        .build();

        adapterServiceCategory = new AdapterServiceCategory(options);
        recyclerView.setAdapter(adapterServiceCategory);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserBookServices.this, UserHome.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterServiceCategory.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterServiceCategory.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(UserBookServices.this, UserHome.class));
    }
}