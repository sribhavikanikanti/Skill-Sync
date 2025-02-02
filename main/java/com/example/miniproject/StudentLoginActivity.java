package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentLoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.studentloginbutton);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        Button signupButton = findViewById(R.id.signup_button);
        if (signupButton != null) {
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signUpIntent = new Intent(StudentLoginActivity.this, StudentSignupActivity.class);
                    signUpIntent.putExtra("user_role", "student");
                    startActivity(signUpIntent);
                }
            });
        }
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Please enter an email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // Login successful
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        Intent mainIntent = new Intent(StudentLoginActivity.this, StudentMainActivity.class);
                        mainIntent.putExtra("user_id", user.getUid());
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                    }
                } else {
                    // Login failed
                    Toast.makeText(StudentLoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void showPopup(String title, String message) {
        new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}







/*package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class StudentLoginActivity extends AppCompatActivity {

    EditText etEmail, etPhone;
    Button btnLogin;
    CardView cardView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.password);
        btnLogin = findViewById(R.id.studentloginbutton);


        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        Button signupButton = findViewById(R.id.signup_button);
        if (signupButton != null) {
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signUpIntent = new Intent(StudentLoginActivity.this, StudentSignupActivity.class);
                    signUpIntent.putExtra("user_role", "student");
                    startActivity(signUpIntent);
                }
            });
        }
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (!isValidPhone(phone)) {
            etPhone.setError("Please enter a valid 10-digit phone number");
            etPhone.requestFocus();
            return;
        }

        // Firebase authentication logic here
        // You can use email and phone for login based on your Firebase setup

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, phone).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Intent mainIntent = new Intent(StudentLoginActivity.this, StudentMainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
            } else {
                // Handle failed login
            }
        });
    }

    private boolean isValidPhone(String phone) {

        return Pattern.matches("\\d{10}", phone);
    }
}







package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class StudentLoginActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etRegisterNumber, etBranch;
    Button btnLogin;
    CardView cardView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.password);
        btnLogin=findViewById(R.id.studentloginbutton);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();




        Button signupButton = findViewById(R.id.signup_button);
        if (signupButton != null) {
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signUpIntent = new Intent(StudentLoginActivity.this, StudentSignupActivity.class);
                    startActivity(signUpIntent);
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perforLogin();
                }
            });
        }
        Button loginbutton = findViewById(R.id.studentloginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(StudentLoginActivity.this, StudentMainActivity.class);
                startActivity(MainIntent);
            }
        });
    }

    private void perforLogin() {
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();


        // Validate name (alphabets only)

        // Validate phone number (10 digits only)
        if (!isValidPhone(phone)) {
            etPhone.setError("Please enter a valid 10-digit phone number");
            etPhone.requestFocus();
            return false;
        }

        // Validate branch (must be "CSE")


        // Validate register number (in specified format)
        String registerNumber;
        if (!isValidRegisterNumber(registerNumber)) {
            etRegisterNumber.setError("Please enter a valid register number");
            etRegisterNumber.requestFocus();
            return false;
        }

        // Validate email (should start with register number followed by @svecw.edu.in)
        if (!isValidEmail(email, registerNumber)) {
            etEmail.setError("Email should start with register number followed by @svecw.edu.in");
            etEmail.requestFocus();
            return false;
        }

        return true;
    }



    private boolean isValidPhone(String phone) {
        return Pattern.matches("\\d{10}", phone);
    }



    private boolean isValidRegisterNumber(String registerNumber) {
        // Example validation logic; adjust as per your requirement
        return Pattern.matches("22b01a05[0-9a-zA-Z][0-9]", registerNumber);
    }

    private boolean isValidEmail(String email, String registerNumber) {
        // Example validation logic; adjust as per your requirement
        String expectedEmail = registerNumber + "@svecw.edu.in";
        return email.startsWith(expectedEmail);
    }*/