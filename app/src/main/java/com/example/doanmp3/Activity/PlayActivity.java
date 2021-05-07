package com.example.doanmp3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.doanmp3.R;

public class PlayActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        setSupportActionBar(toolbar);
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }
}