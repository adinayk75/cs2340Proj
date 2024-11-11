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
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class Accommodation extends AppCompatActivity {

    private LinearLayout accommodationContainer;

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

        accommodationContainer = findViewById(R.id.accommodationContainer);

        FloatingActionButton addAccommodationButton = findViewById(R.id.addAccommodationButton);
        addAccommodationButton.setOnClickListener(v -> {
            showAccommodationForm();
        });

        Button logoutButton = findViewById(R.id.accLogout);
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Accommodation.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

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

        displayAccommodations();
    }

    private void showAccommodationForm() {
        View formView = getLayoutInflater().inflate(R.layout.accomodation_form, null);

        EditText checkInDate = formView.findViewById(R.id.editCheckInDate);
        EditText checkOutDate = formView.findViewById(R.id.editCheckOutDate);
        EditText location = formView.findViewById(R.id.editLocation);
        EditText numRooms = formView.findViewById(R.id.editNumRooms);
        EditText roomType = formView.findViewById(R.id.editRoomType);
        Button submitButton = formView.findViewById(R.id.submitAccommodationButton);

        setContentView(formView);

        submitButton.setOnClickListener(view -> {
            String checkIn = checkInDate.getText().toString().trim();
            String checkOut = checkOutDate.getText().toString().trim();
            String loc = location.getText().toString().trim();
            String numRoom = numRooms.getText().toString().trim();
            String roomTyp = roomType.getText().toString().trim();

            if (checkIn.isEmpty() || checkOut.isEmpty() || loc.isEmpty() || numRoom.isEmpty() || roomTyp.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    saveAccommodationToDatabase(checkIn, checkOut, loc, numRoom, roomTyp);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid number for rooms.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveAccommodationToDatabase(String checkInDate, String checkOutDate, String location, String numRooms, String roomType) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("accommodations").child(userId);

        String id = database.push().getKey();

        if (id != null) {
            AccommodationInfo accommodation = new AccommodationInfo(checkInDate, checkOutDate, location, Integer.parseInt(numRooms), roomType);

            database.child(id).setValue(accommodation);
        }
    }

    private void displayAccommodations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("accommodations").child(userId);

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AccommodationInfo> accommodations = new ArrayList<>();

                // Collect all accommodations into a list
                for (DataSnapshot accommodationSnapshot : snapshot.getChildren()) {
                    AccommodationInfo accommodation = accommodationSnapshot.getValue(AccommodationInfo.class);
                    if (accommodation != null) {
                        accommodations.add(accommodation);
                    }
                }

                // Sort accommodations by check-in date
                accommodations.sort((a1, a2) -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date1 = LocalDate.parse(a1.getCheckInDate(), formatter);
                    LocalDate date2 = LocalDate.parse(a2.getCheckInDate(), formatter);
                    return date1.compareTo(date2);
                });

                // Clear and populate the container with sorted accommodations
                accommodationContainer.removeAllViews();
                for (AccommodationInfo accommodation : accommodations) {
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

                    accommodationContainer.addView(accommodationView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });
    }
}