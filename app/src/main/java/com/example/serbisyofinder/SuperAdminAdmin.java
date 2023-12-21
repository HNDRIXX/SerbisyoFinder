package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.google.firebase.database.FirebaseDatabase;

public class SuperAdminAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterAdmin adapterAdmin;

    String accID;
    FirebaseUser account;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_admin);

        getSupportActionBar().hide();

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.adminList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        account = FirebaseAuth.getInstance().getCurrentUser();
        accID = account.getUid();

        FirebaseRecyclerOptions<Account> options =
                new FirebaseRecyclerOptions.Builder<Account>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Account").orderByChild("name"), Account.class)
                        .build();

        adapterAdmin = new AdapterAdmin(options);
        recyclerView.setAdapter(adapterAdmin);

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
                    }
                }
            });
        }

        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuperAdminAdmin.this, SuperAdminHome.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterAdmin.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterAdmin.stopListening();
    }

    private void filterUsers(String searchText) {
        FirebaseRecyclerOptions<Account> options =
                new FirebaseRecyclerOptions.Builder<Account>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Account").orderByChild("name").startAt(searchText).endAt(searchText + "~"), Account.class)
                        .build();

        adapterAdmin = new AdapterAdmin(options);
        adapterAdmin.startListening();
        recyclerView.setAdapter(adapterAdmin);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(SuperAdminAdmin.this, SuperAdminHome.class));
    }
}