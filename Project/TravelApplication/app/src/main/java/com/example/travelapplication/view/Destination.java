package com.example.travelapplication.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.databinding.DataBindingUtil;

import com.example.travelapplication.R;
import com.example.travelapplication.databinding.TravelFormBinding;
import com.example.travelapplication.model.TravelInfo;
import com.example.travelapplication.utils.FirebaseDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



public class Destination extends AppCompatActivity {

    private static final String DATE_PATTERN =
            "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$";

    private LinearLayout destinationsContainer;

    private void showTravelForm() {
        Map<String, Object> travelData = new HashMap<>();

        Dialog dialog = new Dialog(this);

        TravelInfo travelInfo = new TravelInfo("", "", "");

        // Inflate popup layout with data binding
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        TravelFormBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.travel_form, null, false);
        binding.setTravelInfo(travelInfo);

        dialog.setContentView(binding.getRoot());

        // Error Checking Logic
        Button submitButton = dialog.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;

                if (travelInfo.getLocation().isEmpty()) {
                    binding.travelLocation.setError("Please enter a destination");
                    isValid = false;
                }

                if (travelInfo.getEstimatedStart().isEmpty()) {
                    binding.estimatedStart.setError("Please enter an estimated start date");
                    isValid = false;
                } else if (!travelInfo.getEstimatedStart().matches(DATE_PATTERN)) {
                    binding.estimatedStart.setError("Please enter a valid date (MM/DD/YYYY)");
                    isValid = false;
                }

                if (travelInfo.getEstimatedEnd().isEmpty()) {
                    binding.estimatedEnd.setError("Please enter an estimated end date");
                    isValid = false;
                } else if (!travelInfo.getEstimatedEnd().matches(DATE_PATTERN)) {
                    binding.estimatedEnd.setError("Please enter a valid date (MM/DD/YYYY)");
                    isValid = false;
                }

                String startDate = travelInfo.getEstimatedStart();
                String endDate = travelInfo.getEstimatedEnd();
                if (!checkDate(startDate, endDate)) {
                    binding.estimatedEnd
                            .setError("Ensure your end date takes place after your start date.");
                    isValid = false;
                }

                if (isValid) {
                    DatabaseReference database = FirebaseDatabaseHelper.getInstance().getDatabase();
                    DatabaseReference newTravelRef = database.child("travels").push();

                    // Calculates time between dates

                    startDate = travelInfo.getEstimatedStart();
                    endDate = travelInfo.getEstimatedEnd();
                    String[] startParts = startDate.split("/");
                    String[] endParts = endDate.split("/");

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

                    // Put data in database
                    travelData.put("location", travelInfo.getLocation());
                    travelData.put("estimatedStart", travelInfo.getEstimatedStart());
                    travelData.put("estimatedEnd", travelInfo.getEstimatedEnd());
                    travelData.put("duration", duration);

                    // Adding destination entry
                    addDestinationEntry(travelInfo.getLocation(), duration);

                    newTravelRef.setValue(travelData);

                    // Close the dialog
                    dialog.dismiss();
                }
            }
        });

        Button closeButton = dialog.findViewById(R.id.cancelButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public Boolean checkDate(String startDate, String endDate) {
        if (endDate.substring(endDate.length() - 4)
                .compareTo(startDate.substring(startDate.length() - 4)) < 0) {
            return false;
        } else if ((endDate.substring(endDate.length() - 4)
                .compareTo(startDate.substring(startDate.length() - 4)) == 0)) {
            if ((endDate.substring(endDate.length() - 7, endDate.length() - 5)
                    .compareTo(startDate.substring(endDate.length() - 7,
                            startDate.length() - 5)) < 0)) {
                return false;
            } else if ((endDate.substring(endDate.length() - 7, endDate.length() - 5)
                    .compareTo(startDate.substring(endDate.length() - 7,
                            startDate.length() - 5)) == 0)) {
                if ((endDate.substring(0, endDate.length() - 8)
                        .compareTo(startDate.substring(endDate.length() - 7,
                                startDate.length() - 5)) < 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Calculate button
    private void showInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_date_duration_input, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Enter Details")
                .create();

        EditText startDateInput = dialogView.findViewById(R.id.startDateInput);
        EditText endDateInput = dialogView.findViewById(R.id.endDateInput);
        EditText durationInput = dialogView.findViewById(R.id.durationInput);
        Button submitButton = dialogView.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String startDateStr = startDateInput.getText().toString().trim();
            String endDateStr = endDateInput.getText().toString().trim();
            String durationStr = durationInput.getText().toString().trim();

            calculateMissingField(startDateStr, endDateStr, durationStr);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void calculateMissingField(String startDateStr, String endDateStr, String durationStr) {
        try {
            LocalDate startDate;
            if (startDateStr.isEmpty()) {
                startDate = null;
            } else {
                startDate = LocalDate.parse(startDateStr,
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }

            LocalDate endDate;
            if (endDateStr.isEmpty()) {
                endDate = null;
            } else {
                endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }

            Long duration;
            if (durationStr.isEmpty()) {
                duration = null;
            } else {
                duration = Long.parseLong(durationStr);
            }

            String calculatedStartDate = "";
            String calculatedEndDate = "";
            String calculatedDuration = "";

            if (startDate == null && endDate != null && duration != null) {
                // Calculate Start Date
                startDate = endDate.minusDays(duration);
                calculatedStartDate = startDate
                        .format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                calculatedEndDate = endDateStr;
                calculatedDuration = duration + " days";
                showResult("Start Date is: " + calculatedStartDate);
            } else if (endDate == null && startDate != null && duration != null) {
                // Calculate End Date
                endDate = startDate.plusDays(duration);
                calculatedStartDate = startDateStr;
                calculatedEndDate = endDate
                        .format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                calculatedDuration = duration + " days";
                showResult("End Date is: " + calculatedEndDate);
            } else if (duration == null && startDate != null && endDate != null) {
                // Calculate Duration
                duration = ChronoUnit.DAYS.between(startDate, endDate);
                calculatedStartDate = startDateStr;
                calculatedEndDate = endDateStr;
                calculatedDuration = duration + " days";
                showResult("Duration is: " + calculatedDuration);
            } else {
                showResult("Please fill out exactly two fields.");
                return; // Don't proceed further if the input is invalid
            }

            // Save data to the database
            saveToDatabase(calculatedStartDate, calculatedEndDate, calculatedDuration);

        } catch (Exception e) {
            showResult("Invalid input. Please check the date format and values.");
        }
    }

    private void saveToDatabase(String startDate, String endDate, String duration) {
        // Create a map for the travel data
        Map<String, Object> travelData = new HashMap<>();
        travelData.put("allocatedStartDate", startDate);
        travelData.put("allocatedEndDate", endDate);
        travelData.put("allocatedDuration", duration);

        // Reference to the Firebase database
        DatabaseReference database = FirebaseDatabaseHelper.getInstance().getDatabase();
        DatabaseReference newTravelRef = database.child("travels").push();

        // Store the travel data in the database
        newTravelRef.setValue(travelData)
                .addOnSuccessListener(aVoid -> showResult("Data saved successfully!"))
                .addOnFailureListener(e -> showResult("Failed to save data: " + e.getMessage()));
    }

    private void showResult(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void addDestinationEntry(String location, String duration) {
        int entryCount = destinationsContainer.getChildCount();
        if (entryCount >= 5) {
            destinationsContainer.removeViewAt(0);
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View entryView = inflater.inflate(R.layout.destination_entry,
                destinationsContainer, false);

        TextView destinationLocationTextView =
                entryView.findViewById(R.id.destinationLocationTextView);
        TextView destinationDurationTextView =
                entryView.findViewById(R.id.destinationDurationTextView);

        destinationLocationTextView.setText("Destination: " + location);
        destinationDurationTextView.setText("Duration: " + duration);

        destinationsContainer.addView(entryView);
    }


    // ***********
    // MAIN METHOD
    // ***********


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.destination), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button logTravelButton = findViewById(R.id.logTravelButton);
        Button calculateButton = findViewById(R.id.CalculateVacationTime);

        destinationsContainer = findViewById(R.id.destinationsContainer);

        // Populating the screen with 5 example destinations
        addDestinationEntry("Boston, MA", "10 days");
        addDestinationEntry("New York, NY", "7 days");
        addDestinationEntry("Los Angeles, CA", "5 days");
        addDestinationEntry("Chicago, IL", "3 days");
        addDestinationEntry("San Francisco, CA", "9 days");

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(Destination.this, LoginActivity.class);
            startActivity(intent);
        }

        logTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTravelForm();
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        // Logout
        Button logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Destination.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Navigation Bar
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.destination);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.destination) {
                    Intent intent = new Intent(Destination.this, Destination.class);
                    startActivity(intent);
                } else if (id == R.id.dining) {
                    Intent intent = new Intent(Destination.this, Dining.class);
                    startActivity(intent);
                } else if (id == R.id.accommodation) {
                    Intent intent = new Intent(Destination.this, Accommodation.class);
                    startActivity(intent);
                } else if (id == R.id.transportation) {
                    Intent intent = new Intent(Destination.this, Transportation.class);
                    startActivity(intent);
                } else if (id == R.id.logistics) {
                    Intent intent = new Intent(Destination.this, Logistics.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }
}