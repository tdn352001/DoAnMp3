package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.NewActivity.LoginActivity;
import com.example.doanmp3.NewActivity.MainActivity;
import com.example.doanmp3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.img_splash);
        LoadLocale();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_splash_screen);
        imageView.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigate();
                handler.removeCallbacks(this);
            }
        }, 2000);
    }
    private void LoadLocale(){
        @SuppressLint("CommitPrefEdits")
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String language = preferences.getString("language", "vn");
        SetLocale(language);
    }
    private void SetLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("language", language);
        editor.apply();
    }

    private void Navigate(){
        Intent intent;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}