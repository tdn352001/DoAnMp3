package com.example.doanmp3.NewActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialButton btnLanguage, btnLogout;
    final String[] languages = {"vn", "en"};
    int oldLanguage;
    int checkedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitControls();
        SetUpToolBar();
        LoadLocale();
        HandleEvents();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.tool_bar_settings);
        btnLanguage = findViewById(R.id.btn_language);
        btnLogout = findViewById(R.id.btn_logout);
    }


    private void SetUpToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void LoadLocale(){
        @SuppressLint("CommitPrefEdits")
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String language = preferences.getString("language", "vn");
        checkedItem = GetLocale(language);
        oldLanguage = checkedItem;
    }

    private int GetLocale(String language){
        for(int i = 0; i < languages.length; i++){
            if(language.equals(languages[i]))
                return i;
        }
        return  0;
    }

    private void HandleEvents() {
        btnLanguage.setOnClickListener(v -> ShowChangeLanguageDialog());
        btnLogout.setOnClickListener(v -> ShowDialogLogout());
    }


    private void ShowChangeLanguageDialog() {
        final String[] listLanguages = {"Tiếng Việt", "English"};
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(SettingsActivity.this);
        dialog.setTitle(getResources().getString(R.string.choose_language));
        dialog.setSingleChoiceItems(listLanguages, checkedItem, (dialog1, which) -> checkedItem = which);
        dialog.setPositiveButton(R.string.ok, (dialog12, which) -> {
            if(checkedItem != oldLanguage)
                ShowDialogApplyChange();
            dialog12.dismiss();
        });
        dialog.show();
    }

    private void ShowDialogApplyChange(){
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(SettingsActivity.this);
        dialog.setTitle(R.string.save_settings);
        dialog.setMessage(R.string.restart_to_apply_change);
        dialog.setNegativeButton(R.string.ok, (dialog1, which) -> {
            SetLocale(checkedItem);
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });
        dialog.setPositiveButton(R.string.cancel, (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private void SetLocale(int position) {
        String language = languages[position];
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

    private void ShowDialogLogout(){
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(SettingsActivity.this);
        dialog.setTitle(R.string.logout);
        dialog.setMessage(R.string.are_you_sure);
        dialog.setNegativeButton(R.string.ok, (dialog1, which) -> Logout());
        dialog.setPositiveButton(R.string.cancel, (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}

