package com.example.doanmp3.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.doanmp3.Application.BroadcastReceiver;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static com.example.doanmp3.Application.Notification.CHANNEL_ID;

public class MusicService extends Service {
    public static ArrayList<BaiHat> arrayList;
    public static boolean isAudio;
    public static int Pos;
    int Progress;
    public static boolean random;
    public static boolean repeat;
    public static MediaPlayer mediaPlayer;

    // Action
    final public static int ACTION_PREVIOUS = 1;
    final public static int ACTION_PLAY = 2;
    final public static int ACTION_NEXT = 3;
    final public static int ACTION_CLEAR = 4;
    final public static int ACTION_CHANGE_POS = 5;
    final public static int ACTION_START_PLAY = 7;


    ArrayList<Integer> playedlist;
    Stack<Integer> stack;


    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        stack = new Stack<>();
        playedlist = new ArrayList<>();
        Progress = 0;
        repeat = true;
        random = false;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCompletion(MediaPlayer mp) {
                playedlist.add(Pos);
                stack.push(Pos);
                if (arrayList == null)
                    return;
                if (arrayList.size() == playedlist.size()) {
                    playedlist.clear();
                }
                if (repeat) {
                    if (random) {
                        Random rd = new Random();
                        Pos = rd.nextInt(arrayList.size());
                        while (playedlist.contains(Pos))
                            Pos = rd.nextInt(arrayList.size());
                    } else {
                        Pos++;
                        if (Pos > arrayList.size() - 1)
                            Pos = 0;
                    }
                }
                PlayNhac();
                MusicControlNotification();
                SendActionToActivity(ACTION_CHANGE_POS);
            }
        });

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GetDataBaiHat(intent);
        MusicControlNotification();
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void GetDataBaiHat(Intent intent) {

        if (intent != null) { // Lấy Dữ Liệu Bài Hát
            if (intent.hasExtra("mangbaihat"))
                arrayList = intent.getParcelableArrayListExtra("mangbaihat");
            if (intent.hasExtra("audio"))
                isAudio = intent.getBooleanExtra("audio", false);
            if (intent.hasExtra("pos")) {
                Pos = intent.getIntExtra("pos", 0);
                if (arrayList != null)
                    PlayNhac();
            }


            if (intent.hasExtra("action_activity")) {
                int action = intent.getIntExtra("action_activity", 0);
                ActionControlMusic(action);
            }

            if (intent.hasExtra("action_notification")) {
                int action = intent.getIntExtra("action_notification", 0);
                ActionControlMusic(action);
            }
        }
    }

    // Chơi Nhạc
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PlayNhac() {
        if (!isAudio) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(arrayList.get(Pos).getLinkBaiHat());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try{
                mediaPlayer.reset();
                Uri uri = Uri.parse(arrayList.get(Pos).getLinkBaiHat());
                Log.e("BBB", uri.toString());
                mediaPlayer.setDataSource(getBaseContext(), uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
            catch (Exception ignored){}
        }

        SendActionToActivity(ACTION_START_PLAY);
        SendActionToMain(ACTION_START_PLAY);

    }

    // Action Click Trên Thanh Thông Báo
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ActionControlMusic(int action) {
        switch (action) {
            case ACTION_PREVIOUS:
                ActionPrevious();
                break;
            case ACTION_PLAY:
                ActionPlay();
                break;
            case ACTION_NEXT:
                ActionNext();
                break;
            case ACTION_CLEAR:
                stopSelf();
                break;
            case ACTION_CHANGE_POS:
                PlayNhac();
                break;
        }
        SendActionToActivity(action);
        SendActionToMain(action);
    }


    // Quay Trờ về bài trước
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ActionPrevious() {
        if (random) {
            if (!stack.empty()) {
                Pos = stack.pop();
                if (playedlist.size() > 0)
                    playedlist.remove(playedlist.size() - 1);
            } else
                Pos = 0;
        } else {
            Pos--;
            if (Pos < 0)
                Pos = arrayList.size() - 1;
        }
        MusicControlNotification();
        PlayNhac();
    }

    // resume hoặc pause
    private void ActionPlay() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
        MusicControlNotification();
    }

    // Chuyển đến bài tiếp theo
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ActionNext() {
        playedlist.add(Pos);
        stack.push(Pos);
        if (arrayList.size() == playedlist.size()) {
            playedlist.clear();
        }

        if (random) {
            Random rd = new Random();
            Pos = rd.nextInt(arrayList.size());
            while (playedlist.contains(Pos))
                Pos = rd.nextInt(arrayList.size());
        } else {
            Pos++;
            if (Pos > arrayList.size() - 1)
                Pos = 0;
        }
        MusicControlNotification();
        PlayNhac();
    }


    //Push thông báo
    private void MusicControlNotification() {

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_song)
                .setSubText(arrayList.get(Pos).getTenBaiHat())
                .setContentTitle(arrayList.get(Pos).getTenBaiHat())
                .setContentText(arrayList.get(Pos).getTenAllCaSi())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.ic_prev, "Previoust", getPendingIntent(this, ACTION_PREVIOUS))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1, 3)
                        .setMediaSession(mediaSessionCompat.getSessionToken()));
        if (!isAudio)
            notificationBuilder.setLargeIcon(getBitmapFromURL(arrayList.get(Pos).getHinhBaiHat()));
        else
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.music2));

        if (mediaPlayer.isPlaying())
            notificationBuilder.addAction(R.drawable.ic_pause, "Play", getPendingIntent(this, ACTION_PLAY))
                    .addAction(R.drawable.ic_next, "Next", getPendingIntent(this, ACTION_NEXT))
                    .addAction(R.drawable.ic_clear, "Cancel", getPendingIntent(this, ACTION_CLEAR));
        else
            notificationBuilder.addAction(R.drawable.icon_play, "Play", getPendingIntent(this, ACTION_PLAY))
                    .addAction(R.drawable.ic_next, "Next", getPendingIntent(this, ACTION_NEXT))
                    .addAction(R.drawable.ic_clear, "Cancel", getPendingIntent(this, ACTION_CLEAR));

        startForeground(1, notificationBuilder.build());
    }

    // Lấy Sự Kiện Click từ Thông Báo
    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, BroadcastReceiver.class);
        intent.putExtra("action_notification", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Set Hình ảnh cho thông báo
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }

    }


    // Gửi Action tới activity
    private void SendActionToActivity(int action) {
        Intent intent = new Intent("action_activity");
        intent.putExtra("action", action);
        intent.putExtra("pos", Pos);
        intent.putExtra("isPlaying", mediaPlayer.isPlaying());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void SendActionToMain(int action) {
        Intent intent = new Intent("action_mainactivity");
        intent.putExtra("action", action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        mediaPlayer.seekTo(0);
        mediaPlayer.stop();
        super.onDestroy();
    }
}
