package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doanmp3.Dialog.CustomDialog;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {


    Toolbar toolbar;
    MaterialButton btnLanguage, btnPassword, btnLogout;
    FirebaseUser user;
    ProgressDialog progressDialog;
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
        btnPassword = findViewById(R.id.btn_password);
        btnLogout = findViewById(R.id.btn_logout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
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
        btnPassword.setOnClickListener(v -> ShowDialogChangePassword());
        btnLogout.setOnClickListener(v -> ShowDialogLogout());
    }

    private void ShowDialogChangePassword() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.setContentView(R.layout.dialog_change_password);
        Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
            dialog.setCancelable(true);
        }
        dialog.setCancelable(true);

        TextInputEditText edtPassword, edtNewPassword, edtConfirmPassword;
        MaterialButton btnConfirm, btnCancel;

        edtPassword = dialog.findViewById(R.id.edt_password);
        edtNewPassword = dialog.findViewById(R.id.edt_new_password);
        edtConfirmPassword = dialog.findViewById(R.id.edt_confirm_password);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(v -> dialog.dismiss());



        btnConfirm.setOnClickListener(v -> {
            String password = edtPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if(password.equals("")){
                edtPassword.requestFocus();
                edtPassword.setError(getString(R.string.missing_password));
                return;
            }

            if(newPassword.equals("")){
                edtNewPassword.requestFocus();
                edtNewPassword.setError(getString(R.string.missing_new_password));
                return;
            }

            if(newPassword.length() < 6){
                edtNewPassword.requestFocus();
                edtNewPassword.setError(getString(R.string.not_enough_length_password));
                return;
            }

            if(!newPassword.equals(confirmPassword)){
                edtConfirmPassword.requestFocus();
                edtConfirmPassword.setError(getString(R.string.pasword_not_matching));
                return;
            }
            Tools.hideSoftKeyBoard(this);
            CheckPasswordValid(password, newPassword);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void CheckPasswordValid(String password, String newPassword) {
        progressDialog.show();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ChangePassword(newPassword);
            }     else{
                progressDialog.dismiss();
                Toast.makeText(SettingsActivity.this, R.string.password_invalid, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ChangePassword(String newPassword) {
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(SettingsActivity.this, R.string.change_password_successfully, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingsActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
                Log.e("EEE", task.getException().getMessage());
            }
            progressDialog.dismiss();
        });
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

