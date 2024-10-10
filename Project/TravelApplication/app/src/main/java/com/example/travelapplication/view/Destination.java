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
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.travelapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Destination extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button logoutButton;
    private FirebaseUser user;


    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
            finish();
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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.logistics) {
                    Intent intent = new Intent(Destination.this, Logistics.class);
                    startActivity(intent);
                }
                if (id == R.id.dining) {
                    Intent intent = new Intent(Destination.this, Dining.class);
                    startActivity(intent);
                }
                if (id == R.id.accommodation) {
                    Intent intent = new Intent(Destination.this, Accommodation.class);
                    startActivity(intent);
                }
                if (id == R.id.transportation) {
                    Intent intent = new Intent(Destination.this, Transportation.class);
                    startActivity(intent);
                }
                if (id == R.id.travel) {
                    Intent intent = new Intent(Destination.this, Travel.class);
                    startActivity(intent);
                }
                return false;
            }
        });


    }
}