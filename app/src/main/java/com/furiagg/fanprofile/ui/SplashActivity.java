package com.furiagg.fanprofile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.furiagg.fanprofile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2000; // 2 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Verificar se o usuário já está logado
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent;
            if (currentUser != null) {
                // Se o usuário já estiver logado, vá para MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Caso contrário, vá para LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_TIME);
    }
}