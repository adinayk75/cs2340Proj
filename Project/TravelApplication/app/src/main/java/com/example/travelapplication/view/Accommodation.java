package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Accommodation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accommodation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigation Bar

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.destination) {
                    Intent intent = new Intent(Accommodation.this, Destination.class);
                    startActivity(intent);
                }
                if (id == R.id.dining) {
                    Intent intent = new Intent(Accommodation.this, Dining.class);
                    startActivity(intent);
                }
                if (id == R.id.accommodation) {
                    Intent intent = new Intent(Accommodation.this, Accommodation.class);
                    startActivity(intent);
                }
                if (id == R.id.transportation) {
                    Intent intent = new Intent(Accommodation.this, Transportation.class);
                    startActivity(intent);
                }
                if (id == R.id.travel) {
                    Intent intent = new Intent(Accommodation.this, Travel.class);
                    startActivity(intent);
                }

                return false;
            }
        });
    }
}