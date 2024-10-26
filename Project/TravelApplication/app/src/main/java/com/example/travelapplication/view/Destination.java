package com.example.travelapplication.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Destination extends AppCompatActivity {

    private void showTravelForm() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.travel_form);

        EditText destinationEditText = dialog.findViewById(R.id.travelLocation);
        EditText estimatedStartEditText = dialog.findViewById(R.id.estimatedStart);
        EditText estimatedEndEditText = dialog.findViewById(R.id.estimatedEnd);

        // Error Checking Logic
        Button submitButton = dialog.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;

                if (destinationEditText.getText().toString().isEmpty()) {
                    destinationEditText.setError("Please enter a destination");
                    isValid = false;
                }

                if (estimatedStartEditText.getText().toString().isEmpty()) {
                    estimatedStartEditText.setError("Please enter an estimated start date");
                    isValid = false;
                }

                if (estimatedEndEditText.getText().toString().isEmpty()) {
                    estimatedEndEditText.setError("Please enter an estimated end date");
                    isValid = false;
                }

                if (isValid) {
                    // Submit the form data to the database
                    // ...

                    // Close the dialog
                    dialog.dismiss();
                }
            }
        });

        Button closeButton = dialog.findViewById(R.id.cancelButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.destination), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button logTravelButton = findViewById(R.id.logTravelButton);

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(Destination.this, LoginActivity.class);
            startActivity(intent);
        }

        logTravelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTravelForm();
            }
        });

        // Logout
        Button logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Destination.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Navigation Bar
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.destination);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.destination) {
                    Intent intent = new Intent(Destination.this, Destination.class);
                    startActivity(intent);
                } else if (id == R.id.dining) {
                    Intent intent = new Intent(Destination.this, Dining.class);
                    startActivity(intent);
                } else if (id == R.id.accommodation) {
                    Intent intent = new Intent(Destination.this, Accommodation.class);
                    startActivity(intent);
                } else if (id == R.id.transportation) {
                    Intent intent = new Intent(Destination.this, Transportation.class);
                    startActivity(intent);
                } else if (id == R.id.logistics) {
                    Intent intent = new Intent(Destination.this, Logistics.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }
}