package com.example.miniproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutUsFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        recyclerView = view.findViewById(R.id.eventRecyclerView);

        // Setup BottomNavigationView
        setupBottomNavigationView(view);

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about_us; // Return the layout resource ID for this fragment
    }

    private void setupBottomNavigationView(View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EventsFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.profile) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
                return true;
            }
            return false;
        });
    }
}
