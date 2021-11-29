package com.example.doanmp3.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.R;

public class AllAlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_album);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }

}