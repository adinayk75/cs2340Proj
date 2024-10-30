package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.view.View;
import java.util.ArrayList;

import com.example.travelapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;



public class Logistics extends AppCompatActivity {
    private PieChart pieChart;
    private FirebaseDatabase data;
    private int durations;
    private int allocatedDurations;
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