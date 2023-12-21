package com.example.serbisyofinder;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseOptions;

public class FirebaseManager {
    private static FirebaseApp firebaseApp;

    public static void initializeFirebase(Context context) {
        if (firebaseApp == null) {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setApplicationId("1:182754050767:android:800f3e56efef4a93992dba")
                    .setApiKey("AIzaSyBcZCgSxolWoV9UhYZ175_RUrvGRAOWF94")
                    .setDatabaseUrl("https://serbisyofinderapp-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .build();

            firebaseApp = FirebaseApp.initializeApp(context, firebaseOptions, "firebaseDatabaseConn");
        }
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        if (firebaseApp == null) {
            throw new IllegalStateException("Firebase not initialized. Call initializeFirebase() first.");
        }

        return FirebaseDatabase.getInstance(firebaseApp);
    }
}
