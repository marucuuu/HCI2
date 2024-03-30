package com.example.hci2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    private TextView loginUsersTextView;
    private TextView totalUsersCountTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        loginUsersTextView = findViewById(R.id.login_users_text_view);
        totalUsersCountTextView = findViewById(R.id.total_users_count_text_view);
        logoutButton = findViewById(R.id.log_out); // Assuming the button id is "log_out"

        logoutButton.setOnClickListener(v -> log_out()); // Set OnClickListener for logout button

        // Retrieve total number of user accounts from Firebase Database
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalUsers = snapshot.getChildrenCount();
                totalUsersCountTextView.setText(String.valueOf(totalUsers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void log_out() {
        // Implement your logout logic here
        // For example, clear any session-related data or authentication tokens

        // Redirect to MainActivity
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);

        // Finish the current activity to prevent going back to it using the back button
        finish();
    }
}
