package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.HashMap;
import java.util.Map;

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

    // Adds a collaborator to the list and updates the UI
    private void addCollaborator(String user) {
        if (!collaboratorsList.contains(user)) {
            collaboratorsList.add(user);
            displayCollaborators();
        }
    }

    // Refreshes the collaborators' display
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

    // Shows the invite dialog
    private void showInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite Collaborator");

        // Dialog layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Input prompt
        TextView prompt = new TextView(this);
        prompt.setText("Enter username:");
        layout.addView(prompt);

        // Input field
        EditText usernameInput = new EditText(this);
        usernameInput.setHint("Username");
        layout.addView(usernameInput);

        builder.setView(layout);

        // "Invite" button logic
        builder.setPositiveButton("Invite", (dialog, which) -> {
            String username = usernameInput.getText().toString().trim() + "@travelista.com";
            if (!username.isEmpty()) {
                inviteUser(username);
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // Verifies the username in Firebase and adds to the list if valid
    private void inviteUser(String email) {
        DatabaseReference usersRef = data.getReference("user_mapping");
        usersRef.orderByValue().equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get UID for the invited user
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String collaboratorUid = child.getKey(); // The UID of the invited user
                        String creatorUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // Create shared page after inviting user
                        createSharedPage(creatorUid, collaboratorUid);

                        // Add the collaborator to the list
                        addCollaborator(email); // You can also use UID here if needed
                        Toast.makeText(Logistics.this, "User invited", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    Toast.makeText(Logistics.this, "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error querying user_mapping: " + error.getMessage());
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

        showChart.setOnClickListener(v -> showPieChart());

        // Logout button
        Button logoutButton = findViewById(R.id.logLogout);
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Logistics.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Bottom navigation
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
        inviteCollaboratorsButton.setOnClickListener(v -> showInviteDialog());

        Button sharedPageButton = findViewById(R.id.accessSharedPageButton);

        sharedPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Query the Firebase database asynchronously for the shared page ID
                DatabaseReference sharedPagesRef = FirebaseDatabase.getInstance().getReference("shared_pages");
                String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                sharedPagesRef.orderByChild("creator").equalTo(currentUserUid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String sharedPageId = dataSnapshot.getKey();  // or use appropriate logic to get the shared page ID

                                    // Now navigate to the SharedPageActivity
                                    Intent intent = new Intent(Logistics.this, SharedPageActivity.class);
                                    intent.putExtra("sharedPageId", sharedPageId); // Pass the shared page ID
                                    startActivity(intent);
                                } else {
                                    // No shared page created
                                    Toast.makeText(Logistics.this, "No shared page has been created", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("FirebaseError", "Error retrieving shared page: " + databaseError.getMessage());
                            }
                        });
            }
        });

    }

    private String getSharedPageId() {
        DatabaseReference sharedPagesRef = FirebaseDatabase.getInstance().getReference("shared_pages");

        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final String[] sharedPageId = {""};

        sharedPagesRef.orderByChild("creator").equalTo(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sharedPageId[0] = dataSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error retrieving shared page: " + databaseError.getMessage());
            }
        });

        return sharedPageId[0];
    }

    private void inviteUserToSharedPage(String collaboratorUid) {
        String creatorUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        createSharedPage(creatorUid, collaboratorUid);

    }

    private void getTravelDurations() {
    }

    private void showPieChart() {
        getTravelDurations();
        durations = 10;
        allocatedDurations = 20;
        int total = durations + allocatedDurations;
        float durationProp = 33f;
        float allocatedProp = 67f;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(durationProp, "Estimated Durations"));
        entries.add(new PieEntry(allocatedProp, "Allocated Durations"));

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void createSharedPage(String creatorUid, String collaboratorUid) {
        DatabaseReference sharedPagesRef = FirebaseDatabase.getInstance().getReference("shared_pages");

        String sharedPageId = sharedPagesRef.push().getKey();

        if (sharedPageId != null) {
            Map<String, Object> sharedPageData = new HashMap<>();
            sharedPageData.put("creator", creatorUid);
            sharedPageData.put("collaborator", collaboratorUid);

            sharedPagesRef.child(sharedPageId).setValue(sharedPageData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Logistics.this, "Shared page created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Logistics.this, "Error creating shared page", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}