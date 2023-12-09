package com.example.serbisyofinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminSubService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sub_service);

        getSupportActionBar().hide();

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);
        MaterialButton deleteButton = (MaterialButton) findViewById(R.id.deleteButton);

        TextView subSelect1Value = (TextView) findViewById(R.id.subSelect1);
        TextView subSelect2Value = (TextView) findViewById(R.id.subSelect2);
        TextView subSelect3Value = (TextView) findViewById(R.id.subSelect3);
        TextView subSelect4Value = (TextView) findViewById(R.id.subSelect4);
        TextView subSelect5Value = (TextView) findViewById(R.id.subSelect5);

        String serviceID = getIntent().getStringExtra("serviceID");
        String serviceName = getIntent().getStringExtra("serviceName");
        String subService1 = getIntent().getStringExtra("subService1");
        String subService2 = getIntent().getStringExtra("subService2");
        String subService3 = getIntent().getStringExtra("subService3");
        String subService4 = getIntent().getStringExtra("subService4");
        String subService5 = getIntent().getStringExtra("subService5");

        subSelect1Value.setText(subService1);
        subSelect2Value.setText(subService2);
        subSelect3Value.setText(subService3);
        subSelect4Value.setText(subService4);
        subSelect5Value.setText(subService5);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(deleteButton.getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton(Html.fromHtml("<font color='#FF7F09'>Yes</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Query query = reference.child("Categories").orderByChild("serviceID").equalTo(serviceID);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            snapshot.getRef().removeValue();
                                            startActivity(new Intent(AdminSubService.this, SuccessDeleteService.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminSubService.this, AdminServiceCategory.class));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(AdminSubService.this, AdminServiceCategory.class));
    }
}