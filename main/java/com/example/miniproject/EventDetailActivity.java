package com.example.miniproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class EventDetailActivity extends AppCompatActivity {

    private TextView eventNameTextView, descriptionTextView, registrationStartTextView, registrationDeadlineTextView, durationTextView, navigationTextView, linkTextView, applicableYearsTextView;
    private FirebaseFirestore firestore;
    private ListenerRegistration eventListener;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize UI elements
        eventNameTextView = findViewById(R.id.event_name);
        descriptionTextView = findViewById(R.id.description);
        registrationStartTextView = findViewById(R.id.registration_start);
        registrationDeadlineTextView = findViewById(R.id.registration_deadline);
        durationTextView = findViewById(R.id.duration);
        navigationTextView = findViewById(R.id.navigation);
        linkTextView = findViewById(R.id.link);
        applicableYearsTextView = findViewById(R.id.applicable_years);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Retrieve event ID from Intent
        Intent intent = getIntent();
        String eventId = intent.getStringExtra("event_id");

        if (eventId != null) {
            Log.d("EventDetailActivity", "Received event ID: " + eventId);
            fetchEventDetails(eventId);
        } else {
            Log.d("EventDetailActivity", "No event ID provided");
        }

        // Handle link click
        linkTextView.setOnClickListener(v -> {
            String url = linkTextView.getText().toString().trim();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent webIntent = new Intent(EventDetailActivity.this, WebViewActivity.class);
            webIntent.putExtra("url", url);
            startActivity(webIntent);
        });
    }

    private void fetchEventDetails(String eventId) {
        DocumentReference docRef = firestore.collection("Events").document(eventId);
        eventListener = docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.d("EventDetailActivity", "Listen failed: ", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                // Log the raw data from Firestore
                Log.d("EventDetailActivity", "Fetched event data: " + snapshot.getData());

                // Map the document to EventModel
                EventModel event = snapshot.toObject(EventModel.class);
                if (event != null) {
                    // Log the mapped EventModel object
                    Log.d("EventDetailActivity", "Mapped event: " + event.toString());
                    setEventDetails(event);
                } else {
                    Log.d("EventDetailActivity", "Event object is null");
                }
            } else {
                Log.d("EventDetailActivity", "Document does not exist or data is null");
            }
        });
    }

    private void setEventDetails(EventModel event) {
        eventNameTextView.setText(event.getEventName() != null ? event.getEventName() : "No Name");
        descriptionTextView.setText(event.getDescription() != null ? event.getDescription() : "No Description");
        registrationStartTextView.setText(event.getRegistrationStart() != null ? event.getRegistrationStart() : "No Start Date");
        registrationDeadlineTextView.setText(event.getRegistrationDeadline() != null ? event.getRegistrationDeadline() : "No Deadline");
        durationTextView.setText(event.getDuration() != null ? event.getDuration() : "No Duration");
        navigationTextView.setText(event.getNavigation() != null ? event.getNavigation() : "No Navigation");
        linkTextView.setText(event.getLink() != null ? event.getLink() : "No Link");

        if (event.getApplicableYears() != null && !event.getApplicableYears().isEmpty()) {
            StringBuilder applicableYearsText = new StringBuilder();
            for (String year : event.getApplicableYears()) {
                applicableYearsText.append(year).append(" ");
            }
            applicableYearsTextView.setText(applicableYearsText.toString().trim());
        } else {
            applicableYearsTextView.setText("None");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            eventListener.remove();
        }
    }
}
