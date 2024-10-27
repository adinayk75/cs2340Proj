package com.example.travelapplication.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHelper {
    private static FirebaseDatabaseHelper instance;
    private DatabaseReference database;

    private FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    public DatabaseReference getDatabase() {
        return database;
    }
}
