package com.example.travelapplication.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.example.travelapplication.R;
import com.example.travelapplication.model.AccommodationInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Accommodation extends AppCompatActivity {

    private LinearLayout accommodationContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation);

        // Set window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accommodation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the accommodation container (for dynamic reservation list)
        accommodationContainer = findViewById(R.id.accommodationContainer);

        // Floating Action Button to add accommodation
        FloatingActionButton addAccommodationButton = findViewById(R.id.addAccommodationButton);
        addAccommodationButton.setOnClickListener(v -> {
            showAccommodationForm();
        });

        // Logout Button
        Button logoutButton = findViewById(R.id.accLogout);
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Accommodation.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Navigation Bar
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.accommodation);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.destination) {
                startActivity(new Intent(Accommodation.this, Destination.class));
            } else if (id == R.id.dining) {
                startActivity(new Intent(Accommodation.this, Dining.class));
            } else if (id == R.id.accommodation) {
                startActivity(new Intent(Accommodation.this, Accommodation.class));
            } else if (id == R.id.transportation) {
                startActivity(new Intent(Accommodation.this, Transportation.class));
            } else if (id == R.id.logistics) {
                startActivity(new Intent(Accommodation.this, Logistics.class));
            }
            return true;
        });

        // Display existing accommodations
        displayAccommodations();
    }

    private void showAccommodationForm() {
        // Inflate the form for adding accommodation
        View formView = getLayoutInflater().inflate(R.layout.accomodation_form, null);

        EditText checkInDate = formView.findViewById(R.id.editCheckInDate);
        EditText checkOutDate = formView.findViewById(R.id.editCheckOutDate);
        EditText location = formView.findViewById(R.id.editLocation);
        EditText numRooms = formView.findViewById(R.id.editNumRooms);
        EditText roomType = formView.findViewById(R.id.editRoomType);
        Button submitButton = formView.findViewById(R.id.submitAccommodationButton);

        // Show form in a dialog or directly in the UI
        setContentView(formView);

        submitButton.setOnClickListener(view -> {
            // Get data from form
            String checkIn = checkInDate.getText().toString();
            String checkOut = checkOutDate.getText().toString();
            String loc = location.getText().toString();
            String numRoom = numRooms.getText().toString();
            String roomTyp = roomType.getText().toString();

            // Validate inputs here (optional)

            // Save to Firebase database
            saveAccommodationToDatabase(checkIn, checkOut, loc, numRoom, roomTyp);

            // Close form
            finish();
        });
    }

    private void saveAccommodationToDatabase(String checkInDate, String checkOutDate, String location, String numRooms, String roomType) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("accommodations");
        String id = database.push().getKey();

        if (id != null) {
            AccommodationInfo accommodation = new AccommodationInfo(checkInDate, checkOutDate, location, Integer.parseInt(numRooms), roomType);
            database.child(id).setValue(accommodation);
        }
    }

    private void displayAccommodations() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("accommodations");
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accommodationContainer.removeAllViews(); // Clear previous views

                for (DataSnapshot accommodationSnapshot : snapshot.getChildren()) {
                    AccommodationInfo accommodation = accommodationSnapshot.getValue(AccommodationInfo.class);

                    if (accommodation != null) {
                        // Inflate each reservation view
                        View accommodationView = getLayoutInflater().inflate(R.layout.accommodation_item, null);

                        TextView checkInView = accommodationView.findViewById(R.id.checkInView);
                        TextView checkOutView = accommodationView.findViewById(R.id.checkOutView);
                        TextView locationView = accommodationView.findViewById(R.id.locationView);
                        TextView numRoomsView = accommodationView.findViewById(R.id.numRoomsView);
                        TextView roomTypeView = accommodationView.findViewById(R.id.roomTypeView);

                        checkInView.setText("Check-in: " + accommodation.getCheckInDate());
                        checkOutView.setText("Check-out: " + accommodation.getCheckOutDate());
                        locationView.setText("Location: " + accommodation.getLocation());
                        numRoomsView.setText("Rooms: " + accommodation.getNumRooms());
                        roomTypeView.setText("Room Type: " + accommodation.getRoomType());

                        // Check if the reservation is past
                        LocalDate checkOut = LocalDate.parse(accommodation.getCheckOutDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));


                        accommodationContainer.addView(accommodationView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
            }
        });
    }
}