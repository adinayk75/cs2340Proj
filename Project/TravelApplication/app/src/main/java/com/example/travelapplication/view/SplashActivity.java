package com.example.travelapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

//importing the binding class
import com.example.travelapplication.BR;

import com.example.travelapplication.R;
import com.example.travelapplication.databinding.ActivitySplashBinding;
import com.example.travelapplication.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SplashViewModel viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding.setVariable(BR.viewmodel, viewModel);
        binding.setLifecycleOwner(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, navigate to MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the splash screen activity
            }
        }, 3000);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel.getNavigateToMain().observe(this, shouldNavigate -> {
            if (shouldNavigate) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewModel.startSplashTimer();
    }
}