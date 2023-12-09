package com.example.serbisyofinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;
import java.util.UUID;

public class AdminNewService extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    private MaterialButton addPhotoButton, submitServiceButton;

    ProgressBar progressBar;
    TextView progressBarText;
    ImageView progressBarBackground;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    EditText mainServiceValue, subService1Value, subService2Value, subService3Value, subService4Value, subService5Value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_service);

        getSupportActionBar().hide();

        FirebaseManager.initializeFirebase(this);
        FirebaseDatabase defaultDatabase = FirebaseManager.getFirebaseDatabase();
        DatabaseReference defaultDatabaseReference = defaultDatabase.getReference().child("RealtimeDatabase");

        defaultDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dbConnToken) {
                if (dbConnToken.exists() && !dbConnToken.getValue(String.class).equals("X8tyKCD2E4WLe5n8VvLjXLyXPt82")) {
                    startActivity(new Intent(AdminNewService.this, FirebaseManagerScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialButton backButton = (MaterialButton) findViewById(R.id.backButton);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarText = (TextView) findViewById(R.id.progressBarText);
        progressBarBackground = (ImageView) findViewById(R.id.progressBarBackground);

        imageView = (ImageView) findViewById(R.id.imageView);
        addPhotoButton = (MaterialButton)  findViewById(R.id.addPhotoButton);
        submitServiceButton = (MaterialButton) findViewById(R.id.submitServiceButton);

        mainServiceValue = (EditText) findViewById(R.id.mainServiceValue);
        subService1Value = (EditText) findViewById(R.id.subService1Value);
        subService2Value = (EditText) findViewById(R.id.subService2Value);
        subService3Value = (EditText) findViewById(R.id.subService3Value);
        subService4Value = (EditText) findViewById(R.id.subService4Value);
        subService5Value = (EditText) findViewById(R.id.subService5Value);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        submitServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebaseStorage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminNewService.this, AdminServiceCategory.class));
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebaseStorage() {
        if (mainServiceValue.getText().toString().matches("")) {
            mainServiceValue.setError("Input Required");
            mainServiceValue.requestFocus();
            return;
        }

        if (subService1Value.getText().toString().matches("")) {
            subService1Value.setError("Input Required");
            subService1Value.requestFocus();
            return;
        }

        if (subService2Value.getText().toString().matches("")) {
            subService2Value.setError("Input Required");
            subService2Value.requestFocus();
            return;
        }
        if (subService3Value.getText().toString().matches("")) {
            subService3Value.setError("Input Required");
            subService3Value.requestFocus();
            return;
        }

        if (subService4Value.getText().toString().matches("")) {
            subService4Value.setError("Input Required");
            subService4Value.requestFocus();
            return;
        }

        if (subService5Value.getText().toString().matches("")) {
            subService5Value.setError("Input Required");
            subService5Value.requestFocus();
            return;
        }

        FirebaseDatabase databases = FirebaseDatabase.getInstance();
        String existingPath = "Categories";
        DatabaseReference existingRef = databases.getReference(existingPath);
        String newKey = existingRef.push().getKey();
        DatabaseReference newRef = existingRef.child(newKey);

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            newRef.child("serviceID").setValue(stringBuilder.toString().trim());
                            newRef.child("serviceName").setValue(mainServiceValue.getText().toString().trim());
                            newRef.child("subService1").setValue(subService1Value.getText().toString().trim());
                            newRef.child("subService2").setValue(subService2Value.getText().toString().trim());
                            newRef.child("subService3").setValue(subService3Value.getText().toString().trim());
                            newRef.child("subService4").setValue(subService4Value.getText().toString().trim());
                            newRef.child("subService5").setValue(subService5Value.getText().toString().trim());
                            newRef.child("serviceImg").setValue(imageUrl);

                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            startActivity(new Intent(AdminNewService.this, AdminServiceCategory.class));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } else {
            newRef.child("serviceID").setValue(stringBuilder.toString().trim());
            newRef.child("serviceName").setValue(mainServiceValue.getText().toString().trim());
            newRef.child("subService1").setValue(subService1Value.getText().toString().trim());
            newRef.child("subService2").setValue(subService2Value.getText().toString().trim());
            newRef.child("subService3").setValue(subService3Value.getText().toString().trim());
            newRef.child("subService4").setValue(subService4Value.getText().toString().trim());
            newRef.child("subService5").setValue(subService5Value.getText().toString().trim());
            newRef.child("serviceImage").setValue("");

            progressBar.setVisibility(View.GONE);
            progressBarBackground.setVisibility(View.GONE);
            progressBarText.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            startActivity(new Intent(AdminNewService.this, AdminServiceCategory.class));
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(AdminNewService.this, AdminServiceCategory.class));
    }
}