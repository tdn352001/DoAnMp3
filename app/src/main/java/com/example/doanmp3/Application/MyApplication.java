package com.example.doanmp3.Application;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;

import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;

public class MyApplication extends Application {
    final public static String CHANNEL_ID = "CONTROL_MEDIA_PLAYER";
    private Activity currentActivity = null;
    Handler handler;
    Runnable runnable;
    ProgressDialog progressDialog;
    boolean isShowDialog;
    boolean isInternetAvailable;

    @Override
    public void onCreate() {
        super.onCreate();
        isShowDialog = false;
        isInternetAvailable = true;
        createNotificationChannel();
        CheckInternetConnection();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(null, null);
            channel.setShowBadge(false);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void ShowInternetConnectionFailedDialog() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage(getResources().getString(R.string.connecting_internet));
        progressDialog.setCancelable(false);
        progressDialog.show();
        isShowDialog = true;
    }

    private void CheckInternetConnection() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isInternetAvailable = Tools.isInternetAvailable(getApplicationContext());
                if (isInternetAvailable && isShowDialog) {
                    progressDialog.dismiss();
                    isShowDialog = false;
                } else {
                    if (!isInternetAvailable && !isShowDialog && currentActivity != null) {
                        ShowInternetConnectionFailedDialog();
                    }
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.currentActivity = mCurrentActivity;
    }
}
