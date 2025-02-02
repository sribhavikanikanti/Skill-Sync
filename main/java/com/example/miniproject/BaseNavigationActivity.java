package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private static final String TAG = "BaseNavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hello User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_events);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_hackathons) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HackathonsFragment()).commit();
        } else if (itemId == R.id.nav_internships) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InternshipsFragment()).commit();
        } else if (itemId == R.id.nav_events) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventsFragment()).commit();
        } else if (itemId == R.id.nav_workshops) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkshopsFragment()).commit();
        } else if (itemId == R.id.nav_mentorships) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MentorshipsFragment()).commit();
        } else if (itemId == R.id.nav_certificationCourses) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CertificationCoursesFragment()).commit();
        } else if (itemId == R.id.nav_scholarships) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScholarshipsFragment()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
        } else if (itemId == R.id.nav_logout) {
            Log.d(TAG, "Logging out");
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
