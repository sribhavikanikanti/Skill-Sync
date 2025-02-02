package com.example.miniproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private EditText eventNameEditText, descriptionEditText, registrationStartEditText, registrationDeadlineEditText, linkEditText;
    private Spinner durationSpinner, navigationSpinner;
    private Button btnAddEvent, cancelButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize UI elements
        eventNameEditText = findViewById(R.id.event_name);
        descriptionEditText = findViewById(R.id.description);
        registrationStartEditText = findViewById(R.id.registration_start);
        registrationDeadlineEditText = findViewById(R.id.registration_deadline);
        linkEditText = findViewById(R.id.link);
        durationSpinner = findViewById(R.id.duration_spinner);
        navigationSpinner = findViewById(R.id.navigation_spinner);
        btnAddEvent = findViewById(R.id.add_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set click listener for the Cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up spinners
        ArrayAdapter<CharSequence> navigationAdapter = ArrayAdapter.createFromResource(this,
                R.array.navigation_options, android.R.layout.simple_spinner_item);
        navigationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        navigationSpinner.setAdapter(navigationAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.duration_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(adapter);

        // Set click listener for the Add Event button
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToDatabase();
            }
        });

        // Set click listeners for date EditTexts
        registrationStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(registrationStartEditText);
            }
        });

        registrationDeadlineEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(registrationDeadlineEditText);
            }
        });
    }

    private void addEventToDatabase() {
        String eventName = eventNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String registrationStart = registrationStartEditText.getText().toString().trim();
        String registrationDeadline = registrationDeadlineEditText.getText().toString().trim();
        String link = linkEditText.getText().toString().trim();
        String duration = durationSpinner.getSelectedItem().toString();
        String navigationOption = navigationSpinner.getSelectedItem().toString();
        ArrayList<String> applicableYears = getSelectedYears();

        if (eventName.isEmpty() || description.isEmpty() || registrationStart.isEmpty() || registrationDeadline.isEmpty() || link.isEmpty() || duration.isEmpty() || navigationOption.isEmpty() || applicableYears.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateDates(registrationStart, registrationDeadline)) {
            Toast.makeText(this, "Registration start date must be before the deadline date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique event ID
        String eventId = UUID.randomUUID().toString();

        // Log event ID to ensure it's generated
        Log.d("AddEventActivity", "Generated event ID: " + eventId);

        // Create EventModel object with eventId
        EventModel event = new EventModel(eventId, eventName, description, registrationStart, registrationDeadline,
                duration, navigationOption, link, applicableYears);

        // Add event to Firestore with the generated eventId
        firestore.collection("Events").document(eventId).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private ArrayList<String> getSelectedYears() {
        ArrayList<String> yearsList = new ArrayList<>();

        CheckBox cb1stYear = findViewById(R.id.checkbox_1st_year);
        CheckBox cb2ndYear = findViewById(R.id.checkbox_2nd_year);
        CheckBox cb3rdYear = findViewById(R.id.checkbox_3rd_year);
        CheckBox cb4thYear = findViewById(R.id.checkbox_4th_year);

        if (cb1stYear.isChecked()) {
            yearsList.add("1st Year");
        }
        if (cb2ndYear.isChecked()) {
            yearsList.add("2nd Year");
        }
        if (cb3rdYear.isChecked()) {
            yearsList.add("3rd Year");
        }
        if (cb4thYear.isChecked()) {
            yearsList.add("4th Year");
        }

        return yearsList;
    }

    private boolean validateDates(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return start != null && end != null && start.before(end);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}







//package com.example.miniproject;

//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.UUID;
//
//public class AddEventActivity extends AppCompatActivity {
//
//    private EditText eventNameEditText, descriptionEditText, registrationStartEditText, registrationDeadlineEditText, linkEditText;
//    private Spinner durationSpinner, navigationSpinner;
//    private Button btnAddEvent, cancelButton;
//    private FirebaseFirestore firestore;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_event);
//
//        // Initialize UI elements
//        eventNameEditText = findViewById(R.id.event_name);
//        descriptionEditText = findViewById(R.id.description);
//        registrationStartEditText = findViewById(R.id.registration_start);
//        registrationDeadlineEditText = findViewById(R.id.registration_deadline);
//        linkEditText = findViewById(R.id.link);
//        durationSpinner = findViewById(R.id.duration_spinner);
//        navigationSpinner = findViewById(R.id.navigation_spinner);
//        btnAddEvent = findViewById(R.id.add_button);
//        cancelButton = findViewById(R.id.cancel_button);
//
//        // Initialize Firestore
//        firestore = FirebaseFirestore.getInstance();
//
//        // Set click listener for the Cancel button
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        // Set up spinners
//        ArrayAdapter<CharSequence> navigationAdapter = ArrayAdapter.createFromResource(this,
//                R.array.navigation_options, android.R.layout.simple_spinner_item);
//        navigationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        navigationSpinner.setAdapter(navigationAdapter);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.duration_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        durationSpinner.setAdapter(adapter);
//
//        // Set click listener for the Add Event button
//        btnAddEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addEventToDatabase();
//                finish();
//            }
//        });
//
//        // Set click listeners for date EditTexts
//        registrationStartEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(registrationStartEditText);
//            }
//        });
//
//        registrationDeadlineEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(registrationDeadlineEditText);
//            }
//        });
//    }
//
//    private void addEventToDatabase() {
//        String eventName = eventNameEditText.getText().toString().trim();
//        String description = descriptionEditText.getText().toString().trim();
//        String registrationStart = registrationStartEditText.getText().toString().trim();
//        String registrationDeadline = registrationDeadlineEditText.getText().toString().trim();
//        String link = linkEditText.getText().toString().trim();
//        String duration = durationSpinner.getSelectedItem().toString();
//        String navigationOption = navigationSpinner.getSelectedItem().toString();
//        ArrayList<String> applicableYears = getSelectedYears();
//
//        // Generate a unique event ID
//        String eventId = UUID.randomUUID().toString();
//
//        // Log event ID to ensure it's generated
//        Log.d("AddEventActivity", "Generated event ID: " + eventId);
//
//        // Create EventModel object with eventId
//        EventModel event = new EventModel(eventId, eventName, description, registrationStart, registrationDeadline,
//                duration, navigationOption, link, applicableYears);
//
//        // Add event to Firestore with the generated eventId
//        firestore.collection("Events").document(eventId).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Toast.makeText(AddEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void showDatePickerDialog(final EditText editText) {
//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                    }
//                }, year, month, day);
//        datePickerDialog.show();
//    }
//
//    private ArrayList<String> getSelectedYears() {
//        ArrayList<String> yearsList = new ArrayList<>();
//
//        CheckBox cb1stYear = findViewById(R.id.checkbox_1st_year);
//        CheckBox cb2ndYear = findViewById(R.id.checkbox_2nd_year);
//        CheckBox cb3rdYear = findViewById(R.id.checkbox_3rd_year);
//        CheckBox cb4thYear = findViewById(R.id.checkbox_4th_year);
//
//        if (cb1stYear.isChecked()) {
//            yearsList.add("1st Year");
//        }
//        if (cb2ndYear.isChecked()) {
//            yearsList.add("2nd Year");
//        }
//        if (cb3rdYear.isChecked()) {
//            yearsList.add("3rd Year");
//        }
//        if (cb4thYear.isChecked()) {
//            yearsList.add("4th Year");
//        }
//
//        return yearsList;
//    }
//}
//
//
