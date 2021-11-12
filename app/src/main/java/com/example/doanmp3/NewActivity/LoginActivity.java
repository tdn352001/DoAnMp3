package com.example.doanmp3.NewActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private long backTime;
    NavController navController;
    Handler handler;
    Runnable runnable;
    ProgressDialog progressDialog;
    boolean isShowDialog;
    boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        navController = Navigation.findNavController(this, R.id.fragmentContainerLogin);
        InitCheckConnectionDialog();
        CheckInternetConnection();
    }

    private void InitCheckConnectionDialog() {
        progressDialog= new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.connecting_internet));
        progressDialog.setCancelable(false);
        isShowDialog = false;
    }

    private void CheckInternetConnection() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isInternetAvailable = Tools.isInternetAvailable(LoginActivity.this);
                if(isInternetAvailable && isShowDialog){
                    progressDialog.dismiss();
                    isShowDialog = false;
                }else{
                    if(!isInternetAvailable && !isShowDialog){
                        progressDialog.show();
                        isShowDialog = true;
                    }
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.loginFragment2)
            navController.navigateUp();
        else {
            if (backTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
            }

            backTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}