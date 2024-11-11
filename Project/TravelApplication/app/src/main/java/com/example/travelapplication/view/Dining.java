package com.example.travelapplication.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Dining extends AppCompatActivity {


    private LinearLayout displayReservation;
    private Button toggleSortButton;
    private boolean isSorted = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault());
    private List<DiningInfo> reservationList = new ArrayList<>();



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
        toggleSortButton = findViewById(R.id.toggleSortButton);

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

        toggleSortButton.setOnClickListener(view -> toggleSort());

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

                if (!location.isEmpty() && !time.isEmpty() && !name.isEmpty()) {
                    saveDiningReservation(location, website, time, name);
                    dialog.dismiss();
                } else {
                    Toast.makeText(Dining.this, "Location, Time, and Name are required.", Toast.LENGTH_SHORT).show();
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
                reservationList.clear();
                displayReservation.removeAllViews();

                for (DataSnapshot diningSnapshot : snapshot.getChildren()) {
                    DiningInfo reservation = diningSnapshot.getValue(DiningInfo.class);
                    if (reservation != null) {
                        reservationList.add(reservation);
                    }
                }

                displayReservations();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dining.this, "Failed to load dining reservations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleSort() {
        isSorted = !isSorted;
        displayReservations();
    }

    private void displayReservations() {
        displayReservation.removeAllViews();

        List<DiningInfo> displayList = new ArrayList<>(reservationList);

        if (isSorted) {
            // Sort in descending order by date
            displayList.sort((r1, r2) -> parseDate(r2.getTime()).compareTo(parseDate(r1.getTime())));
        }

        for (DiningInfo reservation : displayList) {
            View reservationView = getLayoutInflater().inflate(R.layout.dining_reservation, null);

            TextView location = reservationView.findViewById(R.id.restaurant_location);
            TextView name = reservationView.findViewById(R.id.restaurant_name);
            TextView time = reservationView.findViewById(R.id.restaurant_time);
            TextView website = reservationView.findViewById(R.id.restaurant_website);

            location.setText("Location: " + reservation.getLocation());
            name.setText("Name: " + reservation.getName());
            time.setText("Time: " + reservation.getTime());
            website.setText("Website: " + reservation.getWebsite());

            // Check if the reservation date has passed and update the color accordingly
            if (hasDatePassed(reservation.getTime())) {
                location.setTextColor(Color.RED);
                name.setTextColor(Color.RED);
                time.setTextColor(Color.RED);
                website.setTextColor(Color.RED);
            }

            displayReservation.addView(reservationView);
        }
        toggleSortButton.setText(isSorted ? "Unsort" : "Sort by Date");
    }


    private boolean hasDatePassed(String reservationTime) {
        Date reservationDate = parseDate(reservationTime);
        return reservationDate != null && reservationDate.before(new Date());
    }

    private Date parseDate(String reservationTime) {
        try {
            return dateFormat.parse(reservationTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0); // Return an early date if parsing fails
        }
    }


}