package com.example.travelapplication.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SharedPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_page);

        String sharedPageId = getIntent().getStringExtra("sharedPageId");

        // Retrieve the shared page data from Firebase using sharedPageId
        getSharedPageData(sharedPageId);
    }

    private void getSharedPageData(String sharedPageId) {
        DatabaseReference sharedPagesRef = FirebaseDatabase.getInstance().getReference("shared_pages");

        sharedPagesRef.child(sharedPageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Display shared page data here
                } else {
                    Toast.makeText(SharedPageActivity.this, "No shared page found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error retrieving shared page: " + databaseError.getMessage());
            }
        });
    }
}