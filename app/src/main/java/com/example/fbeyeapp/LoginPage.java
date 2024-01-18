package com.example.fbeyeapp;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    public String MY_USERNAME="MyUserName";
    public String MY_USERID="MyUserID";
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}
