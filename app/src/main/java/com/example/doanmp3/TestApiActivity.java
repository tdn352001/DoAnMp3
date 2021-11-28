package com.example.doanmp3;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.Service.Tools;

public class TestApiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        String text = "á à ạ â ấ ầ ậ ă ặ ằ ắ ơ ớ ờ ợ ư ự ừ ứ đ i ì í ì i";
        String removeAccent = Tools.removeAccent(text);
        Log.e("EEE", removeAccent);

    }
}