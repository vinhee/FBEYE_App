package com.example.fbeyeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.home) {
                            loadFragment(new HomeFragment());
                            return true;
                        } else if (item.getItemId() == R.id.stats) {
                            loadFragment(new StatsFragment());
                            return true;
                        } else if (item.getItemId() == R.id.profile) {
                            loadFragment(new ProfileFragment());
                            return true;
                        }
                        return false;
                    }
                });

        // Load HomeFragment initially
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment, null)
                .commit();
    }
}
