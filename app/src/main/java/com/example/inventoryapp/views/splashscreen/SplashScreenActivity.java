package com.example.inventoryapp.views.splashscreen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import com.example.inventoryapp.R;
import com.example.inventoryapp.databinding.ActivityMainBinding;
import com.example.inventoryapp.databinding.ActivitySplashScreenBinding;
import com.example.inventoryapp.views.login_logout.LoginActivity;
import com.example.inventoryapp.views.login_logout.RegisterActivity;
import com.example.inventoryapp.views.main.MainActivity;
import android.os.Handler;


import com.bumptech.glide.Glide;


public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding; // Declara una variable de tipo ActivitySplashScreenBinding


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cargarImagenSplash();

    }

    private void cargarImagenSplash() {
        Glide.with(this)
                .load(R.drawable.giphy)
                .centerCrop()
                .into(binding.ivSplashScreen);

        // Inicia la actividad principal después de un retraso
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciarActividadLogin();

            }
        }, 5000);
    }

    private void iniciarActividadLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}