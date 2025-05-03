package com.furiagg.fanprofile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.User;
import com.furiagg.fanprofile.ui.fragments.InterestsFragment;
import com.furiagg.fanprofile.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar Toolbar
        toolbar = findViewById(R.id.toolbar);
        // Comentado para evitar o erro
        // setSupportActionBar(toolbar);

        // Apenas configure o título
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Configurar DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar NavigationView
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        // Configurar BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        // Carregar dados do usuário
        loadUserData();

        // Carregar fragmento inicial
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            currentUser = documentSnapshot.toObject(User.class);
                            updateNavigationHeader();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Tratamento de erro
                    });
        }
    }

    private void updateNavigationHeader() {
        if (navigationView != null) {
            View headerView = navigationView.getHeaderView(0);
            if (headerView != null && currentUser != null) {
                CircleImageView ivUserPhoto = headerView.findViewById(R.id.ivUserPhoto);
                TextView tvUsername = headerView.findViewById(R.id.tvUsername);
                TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);

                tvUsername.setText(currentUser.getName());
                tvUserEmail.setText(currentUser.getEmail());

                if (currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().isEmpty()) {
                    Glide.with(this)
                            .load(currentUser.getPhotoUrl())
                            .placeholder(R.drawable.default_profile)
                            .into(ivUserPhoto);
                }
            }
        }
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks
        int itemId = item.getItemId();

        if (itemId == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
            return true;
        } else if (itemId == R.id.nav_interests) {
            loadFragment(new InterestsFragment());
            return true;
        } else if (itemId == R.id.nav_logout) {
            // Fazer logout
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}