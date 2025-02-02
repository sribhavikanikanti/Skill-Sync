package com.example.miniproject;

public class Admin {
    private String email;
    private String password;

    public Admin() {
        // Default constructor required for Firebase
    }

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
