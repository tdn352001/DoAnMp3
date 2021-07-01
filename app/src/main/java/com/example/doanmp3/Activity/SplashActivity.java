package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.img_splash);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_splash_screen);
        imageView.startAnimation(animation);
        final int[] i = {0};
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                if(i[0] > 3){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    handler.removeCallbacks(this);
                }
                i[0]++;
            }
        }, 500);
    }
}