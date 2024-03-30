package com.example.hci2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Get references to ImageViews
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);
        ImageView imageView5 = findViewById(R.id.imageView5);

        // Set onClickListener for each ImageView
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, HomePage.class));
                animateView(v);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, products_view.class));
                animateView(v);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, HomePage.class));
                animateView(v);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, profile_settings.class));
                animateView(v);
            }
        });

        // Get reference to logout button
        Button logoutButton = findViewById(R.id.logoutButton);

        // Set OnClickListener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call logout method
                logOut();
            }
        });
    }

    // Method to animate the view
    private void animateView(View view) {
        // Load animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        // Start animation on the view
        view.startAnimation(animation);
    }

    // Method to handle logout action
    private void logOut() {
        // Implement your logout logic here
        // For example, redirect to MainActivity
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
        // Finish the current activity to prevent going back to it using the back button
        finish();
    }
}



