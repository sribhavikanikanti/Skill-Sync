package com.example.miniproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends BaseFragment {

    private TextView tvName, tvEmail, tvPhone, tvRegisterNumber, tvBranch;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ProfileFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find the TextView elements
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvRegisterNumber = view.findViewById(R.id.tvRegisterNumber);
        tvBranch = view.findViewById(R.id.tvBranch);

        // Get user email from FirebaseAuth
        String userEmail = mAuth.getCurrentUser().getEmail();

        if (userEmail != null) {
            fetchUserProfile(userEmail);
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchUserProfile(String email) {
        db.collection("students details")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            tvName.setText(document.getString("name"));
                            tvEmail.setText(document.getString("email"));
                            tvPhone.setText(document.getString("phone"));
                            tvRegisterNumber.setText(document.getString("registerNumber"));
                            tvBranch.setText(document.getString("branch"));
                        } else {
                            Toast.makeText(getContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_events; // Return the layout resource ID for this fragment
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
