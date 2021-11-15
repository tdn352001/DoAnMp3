package com.example.doanmp3.NewActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialButton btnLanguage, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitControls();
        SetUpToolBar();
        HandleEvents();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.tool_bar_settings);
        btnLanguage = findViewById(R.id.btn_language);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void HandleEvents() {
        btnLanguage.setOnClickListener(v -> ShowChangeLanguageDialog());
        btnLogout.setOnClickListener(v -> Logout());
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void SetUpToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    private void ShowChangeLanguageDialog() {
        final String[] listLanguages = {"Tiếng Việt", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(R.string.choose_language);
        builder.setSingleChoiceItems(listLanguages, 0, (dialog, which) -> {
            switch (which) {
                case 1:
                    SetLocale("en");
                    break;
                default:
                    SetLocale("vn");
            }
        }).setPositiveButton(R.string.ok, (dialog, which) -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void LoadLocale(){
        @SuppressLint("CommitPrefEdits")
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String language = preferences.getString("language", "vn");
        SetLocale(language);
    }
}

