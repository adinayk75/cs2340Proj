package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;

import com.example.travelapplication.utils.FirebaseDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.travelapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;


public class Logistics extends AppCompatActivity {
    private PieChart pieChart;
    private FirebaseDatabase data;
    private int durations;
    private int allocatedDurations;
    private ArrayList<String> collaboratorsList = new ArrayList<>();
    private LinearLayout collaboratorsContainer;
    private void addCollaborator(String user) {
        if (!collaboratorsList.contains(user)) {
            collaboratorsList.add(user);
            displayCollaborators();
        }
    }
    private void displayCollaborators() {
        collaboratorsContainer.removeAllViews();
        for (String collaborator : collaboratorsList) {
            TextView collaboratorView = new TextView(this);
            collaboratorView.setText(collaborator);
            collaboratorView.setTextSize(18);
            collaboratorView.setTextColor(getResources().getColor(R.color.black));
            collaboratorsContainer.addView(collaboratorView);
        }
    }
    private void showInviteDialog() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite Collaborator");

        // Set up the input layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView to prompt the username
        TextView prompt = new TextView(this);
        prompt.setText("Enter username:");
        layout.addView(prompt);

        // Add the input EditText for the username
        EditText usernameInput = new EditText(this);
        usernameInput.setHint("Username");
        layout.addView(usernameInput);

        builder.setView(layout);

        // Add an "Invite" button to the dialog
        builder.setPositiveButton("Invite", (dialog, which) -> {
            String username = usernameInput.getText().toString().trim();
            if (!username.isEmpty()) {
                inviteUser(username);
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        builder.show();
    }
    private void inviteUser(String username) {
        DatabaseReference usersRef = data.getReference("users"); // Adjust the path if necessary to match your Firebase structure

        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userFound = false;

                // Iterate through all child snapshots to find a matching user
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.exists()) {
                        userFound = true;
                        break;
                    }
                }

                // Display message based on whether a user was found
                if (userFound) {
                    Toast.makeText(Logistics.this, "User invited", Toast.LENGTH_SHORT).show();
                    addCollaborator(username); // Add the collaborator if user exists
                } else {
                    Toast.makeText(Logistics.this, "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error checking user: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = FirebaseDatabaseHelper.getInstance().getFirebaseInstance();
        durations = 0;
        allocatedDurations = 0;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.logistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button showChart = findViewById(R.id.CreateGraphButton);
        pieChart = findViewById(R.id.pieChart);

        showChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPieChart();
            }
        });

        // Logout
        Button logoutButton = findViewById(R.id.logLogout);

        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Logistics.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Navigation Bar

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.destination) {
                    Intent intent = new Intent(Logistics.this, Destination.class);
                    startActivity(intent);
                } else if (id == R.id.dining) {
                    Intent intent = new Intent(Logistics.this, Dining.class);
                    startActivity(intent);
                } else if (id == R.id.accommodation) {
                    Intent intent = new Intent(Logistics.this, Accommodation.class);
                    startActivity(intent);
                } else if (id == R.id.transportation) {
                    Intent intent = new Intent(Logistics.this, Transportation.class);
                    startActivity(intent);
                } else if (id == R.id.logistics) {
                    Intent intent = new Intent(Logistics.this, Logistics.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        collaboratorsContainer = findViewById(R.id.collaborators_container);
        displayCollaborators();

        Button inviteCollaboratorsButton = findViewById(R.id.invite_collaborators_button);

        inviteCollaboratorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInviteDialog();
            }
        });
    }
    private void getTravelDurations() {
        return;
    }
    private void showPieChart() {
        getTravelDurations();
        durations = 10;
        allocatedDurations=20;
        int total = durations + allocatedDurations;
        float durationProp = 33f;
        float allocatedProp = 67f;
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(durationProp, "Estimated Durations"));
        entries.add(new PieEntry(allocatedProp, "Allocated Durations"));

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart
    }
}