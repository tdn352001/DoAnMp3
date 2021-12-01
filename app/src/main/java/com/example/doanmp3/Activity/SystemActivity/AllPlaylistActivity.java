package com.example.doanmp3.Activity.SystemActivity;

import android.os.Bundle;

import com.example.doanmp3.R;

public class AllPlaylistActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_playlist);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}