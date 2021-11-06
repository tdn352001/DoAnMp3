package com.example.doanmp3.Service;

import static com.example.doanmp3.Application.Notification.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.doanmp3.Activity.MainActivity;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicService extends Service {
    public static ArrayList<BaiHat> arrayList;
    public static boolean isAudio;
    public static int Pos;
    int Progress;
    public static boolean random;
    public static boolean repeat = true;
    public static MediaPlayer mediaPlayer;

    // Action
    final public static int ACTION_PREVIOUS = 1;
    final public static int ACTION_PLAY = 2;
    final public static int ACTION_NEXT = 3;
    final public static int ACTION_CLEAR = 4;
    final public static int ACTION_CHANGE_POS = 5;
    final public static int ACTION_START_PLAY = 7;
    final public static int ACTION_PLAY_FALIED = 8;


    ArrayList<Integer> playedlist;
    Stack<Integer> stack;
    private boolean isRecent;


    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        Progress = 0;
        repeat = true;
        random = false;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
            if (intent.hasExtra("mangbaihat")) {
                arrayList = intent.getParcelableArrayListExtra("mangbaihat");
                stack = new Stack<>();
                playedlist = new ArrayList<>();
            }
            if (intent.hasExtra("audio"))
                isAudio = intent.getBooleanExtra("audio", false);
            if (intent.hasExtra("pos")) {
                Pos = intent.getIntExtra("pos", 0);
                if (arrayList != null)
                    PlayNhac();

            }

            if (intent.hasExtra("recent"))
                isRecent = intent.getBooleanExtra("recent", false);

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

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrayList.get(Pos).getLinkBaiHat());
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                UploadToPlayRecent();
                MusicControlNotification();
                if (mp.isPlaying()) {
                    SendActionToActivity(ACTION_START_PLAY);
                    SendActionToMain(ACTION_START_PLAY);
                    mp.setOnCompletionListener(mp1 -> ActionPlayComplete());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            SendActionToActivity(ACTION_PLAY_FALIED);
            SendActionToMain(ACTION_PLAY_FALIED);
        }

    }

    @SuppressLint("StaticFieldLeak")


    // Action Click Trên Thanh Thông Báo
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ActionControlMusic(int action) {
        switch (action) {
            case ACTION_PREVIOUS:
                ActionPrevious();
                break;
            case ACTION_PLAY:
                ActionPlay();
                SendActionToActivity(action);
                SendActionToMain(action);
                break;
            case ACTION_NEXT:
                ActionNext();
                break;
            case ACTION_CLEAR:
                stopSelf();
                SendActionToActivity(action);
                SendActionToMain(action);
                break;
            case ACTION_CHANGE_POS:
                PlayNhac();
                break;
        }

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ActionPlayComplete() {
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
        SendActionToActivity(ACTION_CHANGE_POS);
    }


    //Push thông báo
    private void MusicControlNotification() {
        if (arrayList != null) {
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_song)
                    .setLargeIcon(getBitmapFromURL(arrayList.get(Pos).getHinhBaiHat()))
                    .setSubText(arrayList.get(Pos).getTenBaiHat())
                    .setContentTitle(arrayList.get(Pos).getTenBaiHat())
                    .setContentText(arrayList.get(Pos).getTenAllCaSi())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(R.drawable.ic_prev, "Previoust", getPendingIntent(this, ACTION_PREVIOUS))
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(1, 3)
                            .setMediaSession(mediaSessionCompat.getSessionToken()));

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
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.music2);
        }

    }


    // Gửi Action tới activity
    private void SendActionToActivity(int action) {
        Intent intent = new Intent("action_activity");
        intent.putExtra("action", action);
        intent.putExtra("pos", Pos);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void SendActionToMain(int action) {
        Intent intent = new Intent("action_mainactivity");
        intent.putExtra("action", action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void UploadToPlayRecent() {
        String id = arrayList.get(Pos).getIdBaiHat();
        if (!id.equals("-1") && !isRecent) {
            DataService dataService = APIService.getUserService();
            Call<String> callback = dataService.PlayNhac(MainActivity.user.getIdUser(), id);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = (String) response.body();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    public static void AddtoPlaylist(Context context, BaiHat baiHat) {
        if (CheckExist(baiHat)) {
            Toast.makeText(context, "Đã Tồn Tại", Toast.LENGTH_SHORT).show();
        } else {
            arrayList.add(baiHat);
            Toast.makeText(context, "Đã Thêm", Toast.LENGTH_SHORT).show();
        }
        Log.e("BBBB", baiHat.getTenBaiHat());
    }

    public static boolean CheckExist(BaiHat baiHat) {
        if (baiHat.getIdBaiHat().equals("-1")) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getIdBaiHat().equals("-1")) {
                    if (arrayList.get(i).getLinkBaiHat().equals(baiHat.getLinkBaiHat()))
                        return true;
                }
            }
        } else {
            for (int i = 0; i < arrayList.size(); i++)
                if (arrayList.get(i).getIdBaiHat().equals(baiHat.getIdBaiHat()))
                    return true;
        }

        return false;
    }


    @Override
    public void onDestroy() {
        arrayList = null;
        mediaPlayer.seekTo(0);
        mediaPlayer.stop();
        super.onDestroy();
    }
}