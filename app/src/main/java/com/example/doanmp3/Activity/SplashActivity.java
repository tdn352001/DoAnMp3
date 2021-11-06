package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.NewActivity.MainActivity;
import com.example.doanmp3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.img_splash);

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