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
import com.example.travelapplication.model.AccommodationInfo;
import com.example.travelapplication.model.DiningInfo;
import com.example.travelapplication.model.TravelPostInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class    Transportation extends AppCompatActivity {

    private LinearLayout displayTravelPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transportation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayTravelPost = findViewById(R.id.travelPostContainer);
        FloatingActionButton addTravelPostFloatingButton = findViewById(R.id.add_travel_post_floating_button);
        addTravelPostFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTravelPostDialog();
            }
        });


        // Logout Button
        Button logoutButton = findViewById(R.id.tranLogout);

        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Transportation.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Navigation Bar
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.transportation);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.destination) {
                    Intent intent = new Intent(Transportation.this, Destination.class);
                    startActivity(intent);
                } else if (id == R.id.dining) {
                    Intent intent = new Intent(Transportation.this, Dining.class);
                    startActivity(intent);
                } else if (id == R.id.accommodation) {
                    Intent intent = new Intent(Transportation.this, Accommodation.class);
                    startActivity(intent);
                } else if (id == R.id.transportation) {
                    Intent intent = new Intent(Transportation.this, Transportation.class);
                    startActivity(intent);
                } else if (id == R.id.logistics) {
                    Intent intent = new Intent(Transportation.this, Logistics.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        displayTravelPosts();
    }

    private void showTravelPostDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_travel_post);

        EditText startDateInput = dialog.findViewById(R.id.edit_start_date);
        EditText endDateInput = dialog.findViewById(R.id.edit_end_date);
        EditText destinationInput = dialog.findViewById(R.id.edit_destination);
        EditText accommodationInput = dialog.findViewById(R.id.edit_accommodation);
        EditText diningReservationInput = dialog.findViewById(R.id.edit_dining_reservation);
        EditText ratingInput = dialog.findViewById(R.id.edit_rating);
        EditText notesInput = dialog.findViewById(R.id.edit_notes);

        Button addTravelPostButton = dialog.findViewById(R.id.add_travel_post_button);

        addTravelPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = startDateInput.getText().toString().trim();
                String endDate = endDateInput.getText().toString().trim();
                String destination = destinationInput.getText().toString().trim();
                String accommodation = accommodationInput.getText().toString().trim();
                String diningReservation = diningReservationInput.getText().toString().trim();
                String rating = ratingInput.getText().toString().trim();
                String notes = notesInput.getText().toString().trim();

                if (startDate.isEmpty()) {
                    Toast.makeText(Transportation.this, "Start date cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (endDate.isEmpty()) {
                    Toast.makeText(Transportation.this, "End date cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (destination.isEmpty()) {
                    Toast.makeText(Transportation.this, "Destination cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (accommodation.isEmpty()) {
                    Toast.makeText(Transportation.this, "Accommodation cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (diningReservation.isEmpty()) {
                    Toast.makeText(Transportation.this, "Dining reservation cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (rating.isEmpty()) {
                    Toast.makeText(Transportation.this, "Rating cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (notes.isEmpty()) {
                    Toast.makeText(Transportation.this, "Notes cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                    Date start = dateFormat.parse(startDate);
                    Date end = dateFormat.parse(endDate);

                    if (start.after(end)) {
                        Toast.makeText(Transportation.this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    Toast.makeText(Transportation.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveTravelPost(startDate, endDate, destination, accommodation, diningReservation, rating, notes);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveTravelPost(String startDate, String endDate, String destination, String accommodation, String diningReservation, String rating, String notes) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("travel").child(userId);
        String id = database.push().getKey();

        if (id != null) {
            TravelPostInfo travelPost = new TravelPostInfo(startDate, endDate, destination, accommodation, diningReservation, rating, notes);
            database.child(id).setValue(travelPost);
        }
    }

    private void displayTravelPosts() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("travel").child(userId);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TravelPostInfo> travelPostList = new ArrayList<>();

                // Collect all travel posts into a list
                for (DataSnapshot travelPostSnapshot : snapshot.getChildren()) {
                    TravelPostInfo travelPost = travelPostSnapshot.getValue(TravelPostInfo.class);
                    if (travelPost != null) {
                        travelPostList.add(travelPost);
                    }
                }

                travelPostList.sort((a1, a2) -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    LocalDate date1 = LocalDate.parse(a1.getStartDate(), formatter);
                    LocalDate date2 = LocalDate.parse(a2.getStartDate(), formatter);
                    return date1.compareTo(date2);
                });

                displayTravelPost.removeAllViews();
                for (TravelPostInfo travelPost : travelPostList) {
                    View travelPostView = getLayoutInflater().inflate(R.layout.travel_post_item, null);

                    TextView destinationView = travelPostView.findViewById(R.id.destination_view);
                    TextView startDateView = travelPostView.findViewById(R.id.start_date_view);
                    TextView endDateView = travelPostView.findViewById(R.id.end_date_view);
                    TextView durationView = travelPostView.findViewById(R.id.duration_view);
                    TextView accommodationView = travelPostView.findViewById(R.id.accommodation_view);
                    TextView diningReservationView = travelPostView.findViewById(R.id.dining_reservation_view);
                    TextView ratingView = travelPostView.findViewById(R.id.rating_view);
                    TextView notesView = travelPostView.findViewById(R.id.notes_view);

                    String startDate = travelPost.getStartDate();
                    String endDate = travelPost.getEndDate();
                    String[] startParts = startDate.split("-");
                    String[] endParts = endDate.split("-");

                    int month1 = Integer.parseInt(startParts[0]);
                    int day1 = Integer.parseInt(startParts[1]);
                    int year1 = Integer.parseInt(startParts[2]);

                    int month2 = Integer.parseInt(endParts[0]);
                    int day2 = Integer.parseInt(endParts[1]);
                    int year2 = Integer.parseInt(endParts[2]);

                    LocalDate startDateLocal = LocalDate.of(year1, month1, day1);
                    LocalDate endDateLocal = LocalDate.of(year2, month2, day2);

                    long period = ChronoUnit.DAYS.between(startDateLocal, endDateLocal);
                    String duration = period + " days";

                    destinationView.setText("Destination: " + travelPost.getDestination());
                    startDateView.setText("Start Date: " + travelPost.getStartDate());
                    endDateView.setText("End Date: " + travelPost.getEndDate());
                    durationView.setText("Duration: " + duration);
                    accommodationView.setText("Accommodation: " + travelPost.getAccommodation());
                    diningReservationView.setText("Dining Reservation: " + travelPost.getDiningReservation());
                    ratingView.setText("Rating: " + travelPost.getRating());
                    notesView.setText("Notes: " + travelPost.getNotes());

                    displayTravelPost.addView(travelPostView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}