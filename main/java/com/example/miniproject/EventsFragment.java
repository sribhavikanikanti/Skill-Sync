package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addEvent;
    private EventRecyclerViewAdapter adapter;
    private static final int ADD_EVENT_REQUEST = 1;

    private ArrayList<EventModel> eventList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "EventsFragment";
    private String userRole = "student"; // Default role

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        recyclerView = view.findViewById(R.id.eventRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventRecyclerViewAdapter(getContext(), eventList);
        recyclerView.setAdapter(adapter);
        setupBottomNavigationView(view);

        addEvent = view.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEventActivity.class);
            startActivityForResult(intent, ADD_EVENT_REQUEST);
        });

        // Check for arguments and set user role
        if (getArguments() != null) {
            userRole = getArguments().getString("user_role", "student");
            Log.d(TAG, "Received userRole in events fragment: " + userRole);
        } else {
            Log.d(TAG, "No arguments received");
        }

        // Show/hide FAB based on user role
        if ("admin".equals(userRole)) {
            addEvent.setVisibility(View.VISIBLE);
        } else {
            addEvent.setVisibility(View.GONE);
        }

        loadCurrentEvents();

        return view;
    }

    private void loadCurrentEvents() {
        db.collection("Events")
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EVENT_REQUEST && resultCode == getActivity().RESULT_OK) {
            loadCurrentEvents(); // Refresh the list of events
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
