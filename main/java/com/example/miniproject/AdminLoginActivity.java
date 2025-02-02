package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.adminloginbutton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateAdmin();
            }
        });
    }

    private void authenticateAdmin() {
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("login")
                .whereEqualTo("email", inputEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String storedPassword = document.getString("password");

                            if (storedPassword != null && storedPassword.equals(inputPassword)) {
                                // Example method to set preferences (this method needs to be implemented)
                                preferences.setDataLogin(AdminLoginActivity.this, true);
                                preferences.setDataAs(AdminLoginActivity.this, "admin");

                                Toast.makeText(AdminLoginActivity.this, "Admin login successful", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                                intent.putExtra("user_role", "admin");  // Pass the role as "admin"
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AdminLoginActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Database error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
