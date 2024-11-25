package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editTextUsername;
    private TextInputEditText editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Automatically redirect if a user is already logged in
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, Destination.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        // Create Account Button -> Navigate to Create Account Page
        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(view -> {
            Intent intent =
                    new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String username = String.valueOf(editTextUsername.getText()) + "@travelista.com";
            String password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginActivity.this,
                        "Enter Username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this,
                        "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Store user data in the database
                            storeUserData();

                            // Navigate to Destination Activity
                            Intent intent =
                                    new Intent(LoginActivity.this, Destination.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(LoginActivity.this, "Log in successful",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void storeUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();

            DatabaseReference usersRef =
                    FirebaseDatabase.getInstance().getReference("user_mapping");

            // Check if the user already exists
            usersRef.child(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().exists()) {
                    // Only add user if the uid does not already exist
                    usersRef.child(uid).setValue(email).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "User data saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Error saving user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}