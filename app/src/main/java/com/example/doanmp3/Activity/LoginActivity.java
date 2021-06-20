package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;

public class LoginActivity extends AppCompatActivity {


    private long backtime;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        navController = Navigation.findNavController(this, R.id.fragment);


    }

    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() != R.id.loginFragment)
            navController.navigateUp();
        else {
            if (backtime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, "Nhấn Lần Nữa Để Thoát", Toast.LENGTH_SHORT).show();
            }

            backtime = System.currentTimeMillis();
        }
    }
}