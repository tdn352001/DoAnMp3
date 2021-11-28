package com.example.doanmp3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestApiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), "password1234");


    }
}