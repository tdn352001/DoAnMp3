package com.example.doanmp3.Application;

import static com.example.doanmp3.Application.MyApplication.CHANNEL_ID;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.doanmp3.R;

import java.io.File;
import java.util.Random;

public class CheckDownloadCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(query);
            int index = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            if (cursor.moveToFirst()) {
                String downloadFileLocalUri = cursor.getString(index);
                if (downloadFileLocalUri != null) {
                    File file = new File(downloadFileLocalUri);
                    String fileName = file.getName();
                    PushNotification(context, fileName);
                }
            }
            cursor.close();
        }
    }

    private void PushNotification(Context context, String fileName) {
        Toast.makeText(context, R.string.download_successfully, Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download_done)
                .setContentTitle(context.getString(R.string.download_successfully))
                .setContentText(context.getString(R.string.download_successfully) + " " + fileName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Random random = new Random();
        int notificationId = random.nextInt();
        notificationManager.notify(notificationId, builder.build());
    }
}
