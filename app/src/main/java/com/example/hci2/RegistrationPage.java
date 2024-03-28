package com.example.hci2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationPage extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    // Display error message if any field is empty
                    Toast toast = new Toast(RegistrationPage.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_fill, findViewById(R.id.custom_toast_layout_root));
                    toast.setView(layout);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    return; // Do not proceed with registration if fields are empty
                }

                // Create user account with email and password
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationPage.this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Save additional user data to Firebase Realtime Database or Firestore
                        saveUserDataToDatabase(user.getUid(), email, phone, firstName, lastName);
                        Toast.makeText(RegistrationPage.this, "Registration successful.",
                                Toast.LENGTH_SHORT).show();
                        // Redirect user to MainActivity
                        Intent intent = new Intent(RegistrationPage.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close the current activity to prevent user from returning to it via back button
                    } else {
                        // If registration fails, display a message to the user.
                        Toast toast = new Toast(RegistrationPage.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_regfailed, findViewById(R.id.custom_toast_layout_root));
                        toast.setView(layout);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });
    }

    private void saveUserDataToDatabase(String userId, String email, String phone, String firstName, String lastName) {
        // Save user data to Firebase Realtime Database
        DatabaseReference userRef = mDatabase.child("users").child(userId);
        userRef.child("email").setValue(email);
        userRef.child("phone").setValue(phone);
        userRef.child("firstName").setValue(firstName);
        userRef.child("lastName").setValue(lastName);
    }
}
