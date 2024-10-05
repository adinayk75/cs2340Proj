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
import com.example.travelapplication.databinding.ActivityMainBinding;
import com.example.travelapplication.viewmodel.MainViewModel;
import com.example.travelapplication.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SplashViewModel viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding.setVariable(BR.viewmodel, viewModel);
        binding.setLifecycleOwner(this);
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