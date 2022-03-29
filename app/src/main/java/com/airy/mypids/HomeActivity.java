package com.airy.mypids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airy.mypids.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.homeSelectedStartPidsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PIDSActivity.class);
            startActivity(intent);
        });
    }
}