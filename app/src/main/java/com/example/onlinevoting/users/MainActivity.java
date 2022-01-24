package com.example.onlinevoting.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.onlinevoting.R;
import com.example.onlinevoting.users.ui.election.ElectionFragment;
import com.example.onlinevoting.users.ui.home.HomeFragment;
import com.example.onlinevoting.users.ui.notice.NotificationFragment;
import com.example.onlinevoting.users.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_home :
                        selectorFragment = new HomeFragment();
                        break;

                    case R.id.navigation_notice :
                        selectorFragment = new NotificationFragment();
                        break;

                    case R.id.navigation_election :
                        selectorFragment = new ElectionFragment();
                        break;

                    case R.id.navigation_profile :
                        selectorFragment = new ProfileFragment();
                        break;
                }

                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }

                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}