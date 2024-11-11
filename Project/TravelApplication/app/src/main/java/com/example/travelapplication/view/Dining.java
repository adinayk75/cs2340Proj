package com.example.travelapplication.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelapplication.R;
import com.example.travelapplication.model.DiningInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



public class Dining extends AppCompatActivity {


    private LinearLayout displayReservation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dining), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayReservation = findViewById(R.id.display_reservation);

        FloatingActionButton addReservationFloatingButton = findViewById(R.id.add_reservation_floating_button);
        addReservationFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiningDialog();
            }
        });
        // Logout Button
        Button logoutButton = findViewById(R.id.dinLogout);

        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Dining.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Navigation Bar
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.dining);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.destination) {
                    Intent intent = new Intent(Dining.this, Destination.class);
                    startActivity(intent);
                } else if (id == R.id.dining) {
                    Intent intent = new Intent(Dining.this, Dining.class);
                    startActivity(intent);
                } else if (id == R.id.accommodation) {
                    Intent intent = new Intent(Dining.this, Accommodation.class);
                    startActivity(intent);
                } else if (id == R.id.transportation) {
                    Intent intent = new Intent(Dining.this, Transportation.class);
                    startActivity(intent);
                } else if (id == R.id.logistics) {
                    Intent intent = new Intent(Dining.this, Logistics.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        loadDiningReservations();
    }

    private void showDiningDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_dining_reservation);

        EditText locationInput = dialog.findViewById(R.id.edit_location);
        EditText websiteInput = dialog.findViewById(R.id.edit_website);
        EditText timeInput = dialog.findViewById(R.id.edit_time);
        EditText nameInput = dialog.findViewById(R.id.edit_name);

        Button addReservationButton = dialog.findViewById(R.id.add_reservation_button);

        addReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = locationInput.getText().toString().trim();
                String website = websiteInput.getText().toString().trim();
                String time = timeInput.getText().toString().trim();
                String name = nameInput.getText().toString().trim();

                if (!location.isEmpty() && !time.isEmpty()) {
                    saveDiningReservation(location, website, time, name);
                    dialog.dismiss();
                } else {
                    Toast.makeText(Dining.this, "Location and Time are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void saveDiningReservation(String location, String website, String time, String name) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("dining").child(userId);
        String id = database.push().getKey();

        if (id != null) {
            DiningInfo dining = new DiningInfo(location, time, website, name);
            database.child(id).setValue(dining);
        }
    }

    private void loadDiningReservations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("dining").child(userId);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                displayReservation.removeAllViews();

                for (DataSnapshot diningSnapshot : snapshot.getChildren()) {
                    DiningInfo reservation = diningSnapshot.getValue(DiningInfo.class);

                    if (reservation != null) {
                        View reservationView = getLayoutInflater().inflate(R.layout.dining_reservation, null);

                        TextView location = reservationView.findViewById(R.id.restaurant_location);
                        TextView name = reservationView.findViewById(R.id.restaurant_name);
                        TextView time = reservationView.findViewById(R.id.restaurant_time);
                        TextView website = reservationView.findViewById(R.id.restaurant_website);

                        location.setText("Location: " + reservation.getLocation());
                        name.setText("Name: " + reservation.getName());
                        time.setText("Time: " + reservation.getTime());
                        website.setText("Website: " + reservation.getWebsite());

                        displayReservation.addView(reservationView);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dining.this, "Failed to load dining reservations", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private boolean hasDatePassed(String reservationTime) {
        // Implement logic to check if the reservation date/time has passed
        return false; // Replace this with actual date comparison logic
    }




}