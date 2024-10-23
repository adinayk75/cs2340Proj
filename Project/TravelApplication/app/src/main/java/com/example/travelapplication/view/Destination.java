package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private FirebaseAuth auth;
    private Button logoutButton;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.destination);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.destination), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);

        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(Destination.this, LoginActivity.class);
            startActivity(intent);
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Destination.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Navigation Bar

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
                } else if (id == R.id.travel) {
                    Intent intent = new Intent(Destination.this, Travel.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }
}