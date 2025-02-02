package com.example.miniproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends BaseNavigationActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String userRole;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Assuming userRole is passed to this activity
        userRole = getIntent().getStringExtra("user_role");
        Log.d("AdminMainActivity", "Received userRole in admin main: " + userRole);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin");

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            EventsFragment eventsFragment = new EventsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_role", userRole);
            eventsFragment.setArguments(bundle);
            Log.d("AdminMainActivity", "Setting arguments for EventsFragment: " + userRole);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, eventsFragment).commit();
            navigationView.setCheckedItem(R.id.nav_events);
            getSupportActionBar().setTitle("All Events"); // Set initial title
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("user_role", userRole);
        Log.d("AdminMainActivity", "Setting arguments for fragment: " + userRole);

        int itemId = item.getItemId();
        if (itemId == R.id.nav_hackathons) {
            HackathonsFragment hackathonsFragment = new HackathonsFragment();
            hackathonsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, hackathonsFragment).commit();
            getSupportActionBar().setTitle("Hackathons");
        } else if (itemId == R.id.nav_internships) {
            InternshipsFragment internshipsFragment = new InternshipsFragment();
            internshipsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, internshipsFragment).commit();
            getSupportActionBar().setTitle("Internships");
        } else if (itemId == R.id.nav_events) {
            EventsFragment eventsFragment = new EventsFragment();
            eventsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, eventsFragment).commit();
            getSupportActionBar().setTitle("All Events");
        } else if (itemId == R.id.nav_workshops) {
            WorkshopsFragment workshopsFragment = new WorkshopsFragment();
            workshopsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, workshopsFragment).commit();
            getSupportActionBar().setTitle("Workshops");
        } else if (itemId == R.id.nav_mentorships) {
            MentorshipsFragment mentorshipsFragment = new MentorshipsFragment();
            mentorshipsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mentorshipsFragment).commit();
            getSupportActionBar().setTitle("Mentorships");
        } else if (itemId == R.id.nav_certificationCourses) {
            CertificationCoursesFragment certificationCoursesFragment = new CertificationCoursesFragment();
            certificationCoursesFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, certificationCoursesFragment).commit();
            getSupportActionBar().setTitle("Certification Courses");
        } else if (itemId == R.id.nav_scholarships) {
            ScholarshipsFragment scholarshipsFragment = new ScholarshipsFragment();
            scholarshipsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, scholarshipsFragment).commit();
            getSupportActionBar().setTitle("Scholarships");
        } else if (itemId == R.id.nav_about) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            aboutUsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, aboutUsFragment).commit();
            getSupportActionBar().setTitle("About Us");
        } else if (itemId == R.id.nav_history) {
            HistoryFragment historyFragment = new HistoryFragment();
            historyFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, historyFragment).commit();
            getSupportActionBar().setTitle("History");
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AdminMainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminMainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();  // Close the current activity
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}




