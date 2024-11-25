package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        getSharedPageData(sharedPageId);

        Button backToLogisticsButton = findViewById(R.id.backToLogisticsButton);
        backToLogisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharedPageActivity.this, Logistics.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void getSharedPageData(String sharedPageId) {
        DatabaseReference sharedPagesRef =
                FirebaseDatabase.getInstance().getReference("shared_pages");

        sharedPagesRef.child(sharedPageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int i;
                } else {
                    Toast.makeText(SharedPageActivity.this,
                            "No shared page found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError",
                        "Error retrieving shared page: " + databaseError.getMessage());
            }
        });
    }
}