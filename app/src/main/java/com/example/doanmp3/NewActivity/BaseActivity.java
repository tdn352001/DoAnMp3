package com.example.doanmp3.NewActivity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.Application.MyApplication;

public class BaseActivity extends AppCompatActivity {
    protected MyApplication myApplication;

    private void clearReferences(){
        Activity currActivity = myApplication.getCurrentActivity();
        if (this.equals(currActivity))
            myApplication.setCurrentActivity(null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) this.getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }
}