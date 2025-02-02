package com.example.miniproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WorkshopsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter adapter;
    private ArrayList<EventModel> eventList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "WorkshopsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workshops, container, false);

        recyclerView = view.findViewById(R.id.eventRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventRecyclerViewAdapter(getContext(), eventList);
        recyclerView.setAdapter(adapter);
        setupBottomNavigationView(view);

        loadCurrentWorkshops();

        return view;
    }

    private void loadCurrentWorkshops() {
        db.collection("Events")
                .whereEqualTo("navigation", "Workshops")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            EventModel event = document.toObject(EventModel.class);
                            String registrationDeadline = event.getRegistrationDeadline();
                            Date deadlineDate = parseDate(registrationDeadline);
                            if (deadlineDate != null && !deadlineDate.before(new Date())) {
                                eventList.add(event);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + dateStr, e);
            return null;
        }
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
