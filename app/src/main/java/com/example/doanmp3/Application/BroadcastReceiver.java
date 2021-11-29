package com.example.doanmp3.Application;

import android.content.Context;
import android.content.Intent;

import com.example.doanmp3.Service.MusicService;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra("action_from_service_to_notification", 2);
        Intent intentService = new Intent(context, MusicService.class);
        intentService.putExtra("action_from_notification", action);
        context.startService(intentService);
    }
}
