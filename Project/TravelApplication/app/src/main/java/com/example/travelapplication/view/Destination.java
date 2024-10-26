package com.example.travelapplication.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.databinding.DataBindingUtil;

import com.example.travelapplication.R;
import com.example.travelapplication.databinding.TravelFormBinding;
import com.example.travelapplication.model.TravelInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Destination extends AppCompatActivity {

    private static final String DATE_PATTERN = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$";

    private void showTravelForm() {
        Dialog dialog = new Dialog(this);

        TravelInfo travelInfo = new TravelInfo("", "", "");

        // Inflate popup layout with data binding
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        TravelFormBinding binding = DataBindingUtil.inflate(inflater, R.layout.travel_form, null, false);
        binding.setTravelInfo(travelInfo);

        dialog.setContentView(binding.getRoot());

        // Error Checking Logic
        Button submitButton = dialog.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;

                if (travelInfo.getLocation().isEmpty()) {
                    binding.travelLocation.setError("Please enter a destination");
                    isValid = false;
                }

                if (travelInfo.getEstimatedStart().isEmpty()) {
                    binding.estimatedStart.setError("Please enter an estimated start date");
                    isValid = false;
                } else if (!travelInfo.getEstimatedStart().matches(DATE_PATTERN)) {
                    binding.estimatedStart.setError("Please enter a valid date (MM/DD/YYYY)");
                    isValid = false;
                }

                if (travelInfo.getEstimatedEnd().isEmpty()) {
                    binding.estimatedEnd.setError("Please enter an estimated end date");
                    isValid = false;
                } else if (!travelInfo.getEstimatedEnd().matches(DATE_PATTERN)) {
                    binding.estimatedEnd.setError("Please enter a valid date (MM/DD/YYYY)");
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