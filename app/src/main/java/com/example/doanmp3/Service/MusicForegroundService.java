package com.example.doanmp3.Service;

import static com.example.doanmp3.Application.Notification.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doanmp3.Application.BroadcastReceiver;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MusicForegroundService extends Service {

    MediaPlayer mediaPlayer;
    int currentSong;
    boolean random;
    boolean repeat;
    ArrayList<Song> songs;
    ArrayList<Bitmap> bitmaps;
    ArrayList<Integer> playedList;
    Stack<Integer> playedStack;

    final public static int ACTION_PREVIOUS_SONG = 1;
    final public static int ACTION_PLAY_OR_PAUSE = 2;
    final public static int ACTION_NEXT_SONG = 3;
    final public static int ACTION_CLEAR = 4;
    final public static int ACTION_CHANGE_POS = 5;
    final public static int ACTION_START_PLAY = 7;
    final public static int ACTION_PLAY_FAILED = 8;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        playedList = new ArrayList<>();
        playedStack = new Stack<>();
        bitmaps = new ArrayList<>();
        repeat = true;
        random = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GetSongData(intent);
        MusicControlNotification();
        return START_STICKY;
    }

    // Get Intent
    private void GetSongData(Intent intent) {
        if (intent == null) return;

        //Get DataSong
        if (intent.hasExtra("songs")) {
            // delete old song
            if(songs != null){
                songs.clear();
                bitmaps.clear();
            }
            if(playedList != null){
                playedList.clear();
                playedStack.clear();
            }
            songs = intent.getParcelableArrayListExtra("songs");
            GetBitmapFromSongs();
        }

//        // Get Data Bitmaps
//        if (intent.hasExtra("bitmaps")) {
//            bitmaps = intent.getParcelableArrayListExtra("bitmaps");
//            String message = bitmaps == null ? "Null" : bitmaps.size() + "";
//            Log.e("EEE",message);
//        }
//
//        if (intent.hasExtra("bitmap")) {
//            Bitmap bitmap = intent.getParcelableExtra("bitmap");
//            bitmaps.add(bitmap);
//            String message = bitmaps == null ? "Null" : bitmaps.size() + "";
//            Log.e("EEE",message);
//        }

        // Get Current Song
        if (intent.hasExtra("currentSong")) {
            currentSong = intent.getIntExtra("currentSong", 0);
            PlaySong();
        }

        // Get Action from Activity
        if (intent.hasExtra("action_from_activity")) {
            int action = intent.getIntExtra("action_from_activity", 0);
            HandleActionControlMusic(action);
        }

        // Get action from notification
        if (intent.hasExtra("action_from_notification")) {
            int action = intent.getIntExtra("action_from_notification", 0);
            HandleActionControlMusic(action);
        }
    }

    private void GetBitmapFromSongs(){
        for(Song song : songs){
            Glide.with(getApplication()).asBitmap().load(song.getThumbnail()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    bitmaps.add(resource);
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    // Get Intent from notification
    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, BroadcastReceiver.class);
        intent.putExtra("action_notification", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Push Notification
    private void MusicControlNotification() {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_song)
                .setLargeIcon(GetBitmapOfCurrentSong())
                .setSubText(songs.get(currentSong).getName())
                .setContentTitle(songs.get(currentSong).getName())
                .setContentText(songs.get(currentSong).getAllSingerNames())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.ic_prev, "Previous", getPendingIntent(this, ACTION_PREVIOUS_SONG))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1, 3)
                        .setMediaSession(mediaSessionCompat.getSessionToken()));

        if (mediaPlayer.isPlaying())
            notificationBuilder.addAction(R.drawable.ic_pause, "Play", getPendingIntent(this, ACTION_PLAY_OR_PAUSE));
        else
            notificationBuilder.addAction(R.drawable.icon_play, "Play", getPendingIntent(this, ACTION_PLAY_OR_PAUSE));

        notificationBuilder.addAction(R.drawable.ic_next, "Next", getPendingIntent(this, ACTION_NEXT_SONG))
            .addAction(R.drawable.ic_clear, "Cancel", getPendingIntent(this, ACTION_CLEAR));

        startForeground(1, notificationBuilder.build());
    }

    private Bitmap GetBitmapOfCurrentSong(){
        if(bitmaps == null || bitmaps.size() <= currentSong){
            return BitmapFactory.decodeResource(getResources(), R.drawable.music2);
        }
        return bitmaps.get(currentSong);
    }

    //Handle Action Control Music
    private void HandleActionControlMusic(int action) {
        switch (action) {
            case ACTION_PREVIOUS_SONG:
                ActionPrevious();
                PlaySong();
                break;
            case ACTION_NEXT_SONG:
                ActionNext();
                PlaySong();
                break;
            case ACTION_PLAY_OR_PAUSE:
                ActionPlayOrPause();
                SendActionToPlayActivity(action);
                SendActionToMain(action);
                break;
            case ACTION_CLEAR:
                stopSelf();
                SendActionToPlayActivity(action);
                SendActionToMain(action);
                break;
            case ACTION_CHANGE_POS:
                PlaySong();
                break;
        }
    }

    private void ActionPrevious() {
        if(playedList != null && playedList.size() > 0){
            currentSong = playedList.get(playedList.size() - 1);
            playedList.remove(playedList.size() - 1);
        }else{
            currentSong--;
            if (currentSong < 0)
                currentSong = songs.size() - 1;
        }
    }

    private void ActionNext() {
        AddSongPlayed();
        if(random){
            Random random = new Random();
            currentSong = random.nextInt(songs.size());
            while (playedStack.contains(currentSong))
                currentSong = random.nextInt(songs.size());
        }else{
            currentSong++;
            if (currentSong > songs.size() - 1)
                currentSong = 0;
        }
    }

    private void ActionPlayOrPause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
        MusicControlNotification();
    }


    private void AddSongPlayed(){
        playedList.add(currentSong);
        playedStack.add(currentSong);
        if(playedStack.size() == songs.size())
            playedStack.clear();
    }


    // Send action to Play Activity
    private void SendActionToPlayActivity(int action) {
        Intent intent = new Intent("action_activity");
        intent.putExtra("action", action);
        intent.putExtra("currentSong", currentSong);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // Send action to Main Activity
    private void SendActionToMain(int action) {
        Intent intent = new Intent("action_mainactivity");
        intent.putExtra("action", action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //Play Music
    private void PlaySong() {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.get(currentSong).getLink());
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                MusicControlNotification();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        songs.clear();
        mediaPlayer.stop();
    }
}
