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

public class AdminServiceCategory extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterAdminServiceCategory adapterAdminServiceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_category);
        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(AdminServiceCategory.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton addServiceButton = (MaterialButton) findViewById(R.id.addServiceButton);

        recyclerView = (RecyclerView) findViewById(R.id.serviceCategoryList);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        FirebaseRecyclerOptions<ServiceCategory> options =
                new FirebaseRecyclerOptions.Builder<ServiceCategory>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories").orderByChild("serviceName"), ServiceCategory.class)
                        .build();

        adapterAdminServiceCategory = new AdapterAdminServiceCategory(options);
        recyclerView.setAdapter(adapterAdminServiceCategory);

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminServiceCategory.this, AdminNewService.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminServiceCategory.this, AdminHome.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterAdminServiceCategory.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterAdminServiceCategory.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(AdminServiceCategory.this, AdminHome.class));
    }
}