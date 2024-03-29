package com.example.hci2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView SignUpTextView; // Added TextView for "Forgot Password?"
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        SignUpTextView = findViewById(R.id.sign_up_text_view); // Initialize TextView

        // Set OnClickListener for the "Forgot Password?" TextView
        SignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the RegistrationPage activity when the TextView is clicked
                Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply translate animation to the button
                TranslateAnimation translateAnimation = new TranslateAnimation(0, -20, 0, 0);
                translateAnimation.setDuration(50); // Adjust the duration as per your preference
                translateAnimation.setFillAfter(true);
                loginButton.startAnimation(translateAnimation);

                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Check if email and password fields are not empty
                if (username.isEmpty() || password.isEmpty()) {
                    // Show a toast message indicating the user to enter both email and password
                    Toast toast = new Toast(MainActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_fill, findViewById(R.id.custom_toast_layout_root));
                    toast.setView(layout);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    return; // Exit the method, don't proceed with login
                }

                // Call Firebase Authentication method to sign in with email and password
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String userEmail = user.getEmail();
                                    if (userEmail != null && userEmail.equals("admin@gmail.com")) {
                                        // Redirect to AdminActivity
                                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Redirect to HomePage for non-admin users
                                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast toast = new Toast(MainActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.custom_toast_layout1, findViewById(R.id.custom_toast_layout_root));
                                toast.setView(layout);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            }
        });

    }


}



