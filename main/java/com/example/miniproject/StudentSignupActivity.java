package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StudentSignupActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etRegisterNumber, etBranch, etPassword;
    Button btnAddToProfile;
    CardView cardView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etRegisterNumber = findViewById(R.id.etRegisterNumber);
        etBranch = findViewById(R.id.etBranch);
        etPassword = findViewById(R.id.etPassword);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        btnAddToProfile = findViewById(R.id.signup);

        btnAddToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    performAuth();
                }
            }
        });
    }

    private void performAuth() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    saveUserData(); // Save user data to Firestore
                    sendUserToNextActivity();
                    Toast.makeText(StudentSignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StudentSignupActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserData() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String registerNumber = etRegisterNumber.getText().toString().trim();
        String branch = etBranch.getText().toString().trim();

        // Create a new user document
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("registerNumber", registerNumber);
        user.put("branch", branch);

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Add a new document with the user ID as the document ID
            db.collection("students details").document(userId)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Document added successfully
                            Toast.makeText(StudentSignupActivity.this, "User data saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred
                        Toast.makeText(StudentSignupActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(StudentSignupActivity.this, StudentLoginActivity.class); // Assuming EventsActivity hosts EventsFragment
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean validateFields() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String registerNumber = etRegisterNumber.getText().toString().trim();
        String branch = etBranch.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!isValidName(name)) {
            etName.setError("Please enter alphabets only");
            etName.requestFocus();
            return false;
        }

        if (!isValidPhone(phone)) {
            etPhone.setError("Please enter a valid 10-digit phone number");
            etPhone.requestFocus();
            return false;
        }

        if (!isValidBranch(branch)) {
            etBranch.setError("Branch must be 'CSE'");
            etBranch.requestFocus();
            return false;
        }

        if (!isValidRegisterNumber(registerNumber)) {
            etRegisterNumber.setError("Please enter a valid register number");
            etRegisterNumber.requestFocus();
            return false;
        }

        if (!isValidEmail(email, registerNumber)) {
            etEmail.setError("Email should start with register number followed by @svecw.edu.in");
            etEmail.requestFocus();
            return false;
        }

        if (!isValidPassword(password)) {
            etPassword.setError("Password must be at least 8 characters long and contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidName(String name) {
        return Pattern.matches("[a-zA-Z ]+", name);
    }

    private boolean isValidPhone(String phone) {
        return Pattern.matches("\\d{10}", phone);
    }

    private boolean isValidBranch(String branch) {
        return branch.equalsIgnoreCase("CSE");
    }

    private boolean isValidRegisterNumber(String registerNumber) {
        return Pattern.matches("22b01a05[0-9a-zA-Z][0-9]", registerNumber);
    }

    private boolean isValidEmail(String email, String registerNumber) {
        String expectedEmail = registerNumber + "@svecw.edu.in";
        return email.startsWith(expectedEmail);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }
}
