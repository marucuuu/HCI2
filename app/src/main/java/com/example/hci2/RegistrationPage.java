package com.example.hci2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
                    Toast.makeText(RegistrationPage.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return; // Do not proceed with registration if fields are empty
                }
                if (!isPasswordValid(password)) {
                    // Display error message if password is not valid
                    Toast.makeText(RegistrationPage.this, "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.", Toast.LENGTH_SHORT).show();
                    return; // Do not proceed with registration if password is not valid
                }

                // Create user account with email and password
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationPage.this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Check if the phone number already exists in the database
                        checkPhoneNumberExists(phone, user.getUid(), email, firstName, lastName);
                    } else {
                        // If registration fails, display a message to the user.
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // If the exception is due to email already being registered
                            Toast.makeText(RegistrationPage.this, "This email is already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                        } else {
                            // For other authentication failures
                            Toast.makeText(RegistrationPage.this, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean isPasswordValid(String password) {
        // Define your password policy here
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    private void checkPhoneNumberExists(String phone, String userId, String email, String firstName, String lastName) {
        DatabaseReference usersRef = mDatabase.child("users");
        usersRef.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If phone number already exists in the database, display an error toast
                    Toast.makeText(RegistrationPage.this, "This phone number is already registered. Please use a different phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // If phone number does not exist, save user data to Firebase Realtime Database
                    saveUserDataToDatabase(userId, email, phone, firstName, lastName);
                    Toast.makeText(RegistrationPage.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                    // Redirect user to MainActivity
                    Intent intent = new Intent(RegistrationPage.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity to prevent user from returning to it via back button
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(RegistrationPage.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
