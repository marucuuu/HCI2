package com.example.hci2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profile_settings extends AppCompatActivity {

    private TextView currentEmailEditText;
    private EditText currentPhoneEditText;
    private EditText currentFirstNameEditText;
    private EditText currentLastNameEditText;
    private Button updateButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        currentEmailEditText = findViewById(R.id.current_email_text_view);
        currentPhoneEditText = findViewById(R.id.current_phone_text_view);
        currentFirstNameEditText = findViewById(R.id.current_first_name_text_view);
        currentLastNameEditText = findViewById(R.id.current_last_name_text_view);
        updateButton = findViewById(R.id.update_button);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child("users").child(userId);

            // Retrieve current user data and populate EditText fields
            userRef.get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);

                    currentEmailEditText.setText(email);
                    currentPhoneEditText.setText(phone);
                    currentFirstNameEditText.setText(firstName);
                    currentLastNameEditText.setText(lastName);
                }
            });

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get updated values from EditText fields
                    String updatedEmail = currentEmailEditText.getText().toString();
                    String updatedPhone = currentPhoneEditText.getText().toString();
                    String updatedFirstName = currentFirstNameEditText.getText().toString();
                    String updatedLastName = currentLastNameEditText.getText().toString();

                    // Update user data in Firebase
                    userRef.child("email").setValue(updatedEmail);
                    userRef.child("phone").setValue(updatedPhone);
                    userRef.child("firstName").setValue(updatedFirstName);
                    userRef.child("lastName").setValue(updatedLastName);

                    Toast toast = new Toast(profile_settings.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_green, findViewById(R.id.custom_toast_layout_root));
                    toast.setView(layout);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    // Notify user about successful update
                    // You can display a Toast or Snackbar here
                }
            });
        }
    }
}
