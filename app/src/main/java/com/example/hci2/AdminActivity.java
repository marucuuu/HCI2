package com.example.hci2;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        loginUsersTextView = findViewById(R.id.login_users_text_view);
        totalUsersCountTextView = findViewById(R.id.total_users_count_text_view);


        // Retrieve currently logged-in users (if applicable)
        // Example:
        // loginUsersTextView.setText("User1, User2, User3");

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
}
