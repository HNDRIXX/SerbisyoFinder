package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Message extends AppCompatActivity {
    String providerID, clientID, senderID;
    EditText messageTextValue;
    RecyclerView recyclerView;
    AdapterMessages adapterMessages;
    MaterialButton sendButton, backButton;

    String accID;
    String scheduleID;
    String youName;
    DatabaseReference reference;
    FirebaseUser account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(Message.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String providerID = getIntent().getStringExtra("providerID");
        String clientID = getIntent().getStringExtra("clientID");
        String clientName = getIntent().getStringExtra("clientName");
        String providerName = getIntent().getStringExtra("providerName");

        messageTextValue = (EditText) findViewById(R.id.messageTextValue);
        sendButton = (MaterialButton) findViewById(R.id.sendButton);
        backButton = (MaterialButton) findViewById(R.id.backButton);

        scheduleID = getIntent().getStringExtra("scheduleID");

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Account");
        accID = account.getUid();

        reference.child(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account userProfile = snapshot.getValue(Account.class);

                youName = userProfile.name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.messagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Messages")
                                .orderByChild("scheduleID").equalTo(scheduleID), Messages.class)
                        .build();

        adapterMessages = new AdapterMessages(options, accID);
        recyclerView.setAdapter(adapterMessages);

        adapterMessages.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int itemCounts = adapterMessages.getItemCount();
                if (itemCounts > 0) {
                    recyclerView.smoothScrollToPosition(itemCounts - 1);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageTextValue.getText().toString().trim();

                if (!message.isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    String path = "Messages";
                    DatabaseReference databaseReference = database.getReference(path);

                    String key = databaseReference.push().getKey();

                    DatabaseReference reference = databaseReference.child(key);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                    String sentDate = dateFormat.format(new Date());
                    String sentTime = timeFormat.format(Calendar.getInstance().getTime());

                    Map<String, Object> dataToSet = new HashMap<>();
                    dataToSet.put("scheduleID", scheduleID);
                    dataToSet.put("providerID", providerID);
                    dataToSet.put("clientID", clientID);
                    dataToSet.put("senderID", accID);
                    dataToSet.put("message", messageTextValue.getText().toString().trim());
                    dataToSet.put("senderName", youName);
                    dataToSet.put("sentDate", sentDate);
                    dataToSet.put("sentTime", sentTime);

                    reference.updateChildren(dataToSet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                messageTextValue.getText().clear();
                                Toast.makeText(Message.this, "Message Send Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Message.this, "Failed to send.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Message.this, "Message is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapterMessages.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterMessages.stopListening();
    }
}