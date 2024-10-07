package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Destination extends AppCompatActivity {
    FirebaseAuth auth;
    Button logoutButton;
    FirebaseUser user;

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
    }
}