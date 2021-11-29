package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    private long backTime;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        navController = Navigation.findNavController(this, R.id.fragmentContainerLogin);

    }



    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.loginFragment2)
            navController.navigateUp();
        else {
            if (backTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
            }

            backTime = System.currentTimeMillis();
        }
    }

}