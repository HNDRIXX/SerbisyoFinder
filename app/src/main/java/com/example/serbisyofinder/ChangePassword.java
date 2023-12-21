package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangePassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseUser account;

    RelativeLayout mainLayout;

    String accID;
    String userID, name, address, gender, phoneNum, email, role, occupation, starRating, mainService, earnings, countBookings;

    ProgressBar progressBar;
    TextView progressBarText;
    ImageView progressBarBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarText = (TextView) findViewById(R.id.progressBarText);
        progressBarBackground = (ImageView) findViewById(R.id.progressBarBackground);

        MaterialButton savePassBtn = (MaterialButton) findViewById(R.id.savePassBtn);
        EditText newPasswordValue = (EditText) findViewById(R.id.newPasswordValue);

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Account");
        accID = account.getUid();

        reference.child(accID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account userProfile = snapshot.getValue(Account.class);

                userID = userProfile.accID;
                name = userProfile.name;
                address = userProfile.address;
                gender = userProfile.gender;
                email = userProfile.email;
                phoneNum = userProfile.phoneNum;
                role = userProfile.role;
                countBookings = userProfile.countBookings;
                earnings = userProfile.earnings;
                occupation = userProfile.occupation;
                starRating = userProfile.starRating;
                mainService = userProfile.mainService;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        savePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = newPasswordValue.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword)){
                    newPasswordValue.setError("Enter 6 alphanumeric password!");
                    return;
                }

                if (newPasswordValue.length() < 6) {
                    newPasswordValue.setError("Password must at least 6 alphanumeric value!");
                    return;
                }

                mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                progressBar.setVisibility(View.VISIBLE);
                progressBarBackground.setVisibility(View.VISIBLE);
                progressBarText.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    newPasswordValue.setText("");

                                    reference = FirebaseDatabase.getInstance().getReference("Account").child(accID);

                                    Account acc = new Account(userID, name, address, gender, phoneNum, email, newPassword, role, occupation, starRating,  mainService, earnings,  countBookings);

                                    reference.setValue(acc);

                                    Toast.makeText(ChangePassword.this, "Password Updated!", Toast.LENGTH_LONG).show();

                                    progressBar.setVisibility(View.GONE);
                                    progressBarBackground.setVisibility(View.GONE);
                                    progressBarText.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    onBackPressed();
                                }
                            }
                    });
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
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}