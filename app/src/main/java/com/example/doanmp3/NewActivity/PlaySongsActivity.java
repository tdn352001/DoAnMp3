package com.example.doanmp3.NewActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.Fragment.MainFragment.ListSongPlayingFragment;
import com.example.doanmp3.Fragment.MainFragment.SongPlayingFragment;
import com.example.doanmp3.NewAdapter.ViewPager2StateAdapter;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.ItemClick;
import com.example.doanmp3.Service.MusicForegroundService;
import com.example.doanmp3.Service.NewDataService;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySongsActivity extends AppCompatActivity implements ItemClick {

    ConstraintLayout layoutPlay;
    ImageButton btnExit, btnOptions;
    TextView tvSongName, tvSingersName;
    CircleIndicator3 circleIndicator3;
    ViewPager2 viewPager;
    SeekBar timeBar;
    TextView tvCurrentTime, tvTotalTime;
    ImageButton btnRandom, btnPrev, btnPlay, btnNext, btnLoop;
    LinearLayout layoutInteractive;
    ImageButton btnLove;
    MaterialButton btnComments;


    ListSongPlayingFragment listFragment;
    SongPlayingFragment songFragment;
    ViewPager2StateAdapter viewPagerAdapter;
    Handler handler;
    SimpleDateFormat simpleDateFormat;
    ArrayList<Song> songs;
    int currentSong;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("currentSong")) {
                currentSong = intent.getIntExtra("currentSong", 0);
            }
            if (intent.hasExtra("action")) {
                int action = intent.getIntExtra("action", 0);
                HandleActionFromService(action);
            }
        }
    };

    MusicForegroundService musicService;
    boolean isBoundServiceConnected;
    boolean isMusicServiceRunning;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBoundServiceConnected = true;
            MusicForegroundService.MusicBinder musicBinder = (MusicForegroundService.MusicBinder) service;
            musicService = musicBinder.getService();
            GetRandomAndLoopState();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBoundServiceConnected = false;
        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, 1000);
            if (musicService == null) return;
            int progress = musicService.getSongProgress();
            timeBar.setProgress(progress);
            tvCurrentTime.setText(simpleDateFormat.format(progress));
        }
    };

    /* ========= ON CREATE =========*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_from_service"));
        InitControls();
        InitViewPagerAdapter();
        SetUpViewPager();
        GetDataSongs();
        HandleEvent();
    }

    /* ========= InitControls =========*/
    @SuppressLint("SimpleDateFormat")
    private void InitControls() {
        layoutPlay = findViewById(R.id.layout_play_activity);
        btnExit = findViewById(R.id.btn_finish_activity_play);
        btnOptions = findViewById(R.id.btn_options_activity_play);
        tvSongName = findViewById(R.id.tv_name_song);
        tvSingersName = findViewById(R.id.tv_name_singers);
        circleIndicator3 = findViewById(R.id.circle_indicator3_play_songs_activity);
        viewPager = findViewById(R.id.viewpager_play_activity);
        timeBar = findViewById(R.id.seekbar_time);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);
        btnRandom = findViewById(R.id.btn_random);
        btnPrev = findViewById(R.id.btn_prev);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnLoop = findViewById(R.id.btn_loop);
        layoutInteractive = findViewById(R.id.layout_interactive_song);
        btnLove = findViewById(R.id.btn_like_song);
        btnComments = findViewById(R.id.btn_comments_song);
        handler = new Handler();
        simpleDateFormat = new SimpleDateFormat("mm:ss");
    }

    /* ========= InitViewPagerAdapter =========*/
    private void InitViewPagerAdapter() {
        listFragment = new ListSongPlayingFragment();
        songFragment = new SongPlayingFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(listFragment);
        fragments.add(songFragment);
        viewPagerAdapter = new ViewPager2StateAdapter(this, fragments, null);
    }

    /* ========= SetUpViewPager =========*/
    private void SetUpViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        circleIndicator3.setViewPager(viewPager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                HideLayoutInteractive(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (positionOffset == 0) {
                    return;
                }
                HideLayoutInteractive(positionOffset);
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);
    }


    private void HideLayoutInteractive(float positionOffset){
        float marginBottom = layoutInteractive.getHeight() * (positionOffset - 1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, (int) marginBottom);
        layoutInteractive.setLayoutParams(layoutParams);
    }

    /* ========= GetDataSongs =========*/
    private void GetDataSongs() {
        Playlist myPlaylist = new Playlist("1", "BlackPink in your area", "https://filenhacmp3.000webhostapp.com/file/6PlaylistBlackPink in your area.jpg");
        currentSong = 0;
        NewDataService dataService = APIService.newService();
        Call<List<Song>> callback = dataService.getSongsFromPlaylistId(myPlaylist.getId());
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                GetDataSongFromApi(response);
                StartForegroundService();
                StartBoundService();
                isMusicServiceRunning = true;
                listFragment.SetGenre("Thể loại: ", "List PlackPink Songs");
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                GetDataSongs();
            }
        });
    }

    /* ========= GetDataSongFromApi =========*/
    private void GetDataSongFromApi(Response<List<Song>> response) {
        songs = (ArrayList<Song>) response.body();
        if (songs == null) {
            songs = new ArrayList<>();
        }
        listFragment.SetUpRecycleView(songs);
    }

    /* ========= HandleEvent =========*/
    private void HandleEvent() {
        btnPlay.setOnClickListener(v -> {
            if (musicService != null) {
                musicService.ActionPlayOrPause();
                HandleActionEventPlayOrPauseMusic();
            }
        });
        btnNext.setOnClickListener(v -> {
            if (musicService != null)
                musicService.ActionNext();
        });
        btnPrev.setOnClickListener(v -> {
            if (musicService != null) musicService.ActionPrevious();
        });
        btnRandom.setOnClickListener(v -> {
            if (musicService != null) {
                int resId = musicService.ChangeRandomState();
                btnRandom.setImageResource(resId);
            }
        });
        btnLoop.setOnClickListener(v -> {
            if (musicService != null) {
                int resId = musicService.ChangeLoopState();
                btnLoop.setImageResource(resId);
            }
        });
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (musicService != null)
                    musicService.setSongProgress(seekBar.getProgress());
            }
        });
    }


    /* ========= onItemClick =========*/
    @Override
    public void onItemClick(int position) {
        Song song = listFragment.getSongs().get(position);
        currentSong = position;
        songFragment.SetSongInfo(song);
        Intent MusicService = new Intent(getApplicationContext(), MusicForegroundService.class);
        MusicService.putExtra("currentSong", currentSong);
        startService(MusicService);
    }

    /* ========= SetBackground =========*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void SetBackground(Song song, int position) {
        Bitmap bitmapThumbnail = listFragment.getBitmap(position);
        Bitmap bitmapCropped = Tools.cropBitmap(bitmapThumbnail, 25);
        Bitmap bitmapSaturationChange = Tools.updateHSV(bitmapCropped, 10f, 1f, 0);
        Bitmap bitmapContrastChange = Tools.changeBitmapContrastBrightness(bitmapSaturationChange, 2, -50);
        Bitmap bitmapBlurred = Tools.blurBitmap(PlaySongsActivity.this, bitmapContrastChange, 25f);
        Drawable backgroundDrawables = new BitmapDrawable(getResources(), bitmapBlurred);
        layoutPlay.setBackground(backgroundDrawables);
    }


    /*
     *
     *  ===================== SERVICE ============================
     *
     * */


    private void SendActionToService(int action) {
        Intent intent = new Intent(this, MusicForegroundService.class);
        intent.putExtra("action_from_activity", action);
        startService(intent);
    }

    /* ========= StartForegroundService =========*/
    private void StartForegroundService() {
        if (songs == null) {
            return;
        }

        Intent MusicService = new Intent(getApplicationContext(), MusicForegroundService.class);
        MusicService.putExtra("songs", songs);
        MusicService.putExtra("currentSong", currentSong);
        startService(MusicService);
    }

    /* ========= StartBoundService =========*/
    private void StartBoundService() {
        Intent intent = new Intent(getApplicationContext(), MusicForegroundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /* ========= HandleActionFromService =========*/
    private void HandleActionFromService(int action) {
        switch (action) {
            case MusicForegroundService.ACTION_START_PLAY:
                HandleActionStartPlayMusic();
                break;
            case MusicForegroundService.ACTION_PLAY_OR_PAUSE:
                HandleActionEventPlayOrPauseMusic();
                break;
            case MusicForegroundService.ACTION_CLEAR:
                HandleActionStopService();
                break;
            case MusicForegroundService.ACTION_PLAY_FAILED:
                HandleActionPlayMusicFailed();
                break;
            case MusicForegroundService.ACTION_PLAY_COMPLETED:
                HandleActionPlayCompleted();
                break;
        }
    }

    private void HandleActionStartPlayMusic() {
        listFragment.ChangeItemSelected(currentSong);
        listFragment.SetSongInfo(songs.get(currentSong).getName(), songs.get(currentSong).getAllSingerNames());
        songFragment.SetSongInfo(songs.get(currentSong));
        SetTitleForToolbar();
        setSongDuration();
        btnPlay.setImageResource(R.drawable.ic_pause_circle_outline);
        timeBar.setProgress(0);
        handler.postDelayed(runnable, 1000);
    }

    private void HandleActionEventPlayOrPauseMusic() {
        int resId = musicService.isMediaPlaying();
        btnPlay.setImageResource(resId);
    }

    private void HandleActionStopService() {
        isMusicServiceRunning = false;
        btnPlay.setImageResource(R.drawable.ic_play_circle_outline);
    }

    private void HandleActionPlayMusicFailed() {
        Toast.makeText(this, getString(R.string.play_failed), Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(runnable);
    }

    private void HandleActionPlayCompleted() {
        handler.removeCallbacks(runnable);
    }

    private void setSongDuration() {
        if (musicService == null) {
            return;
        }
        tvTotalTime.setText(simpleDateFormat.format(musicService.getSongDuration()));
        timeBar.setMax(musicService.getSongDuration());
    }

    private void GetRandomAndLoopState() {
        if (musicService == null) {
            return;
        }
        int resIdRandom = musicService.getRandomState();
        int resIdLoop = musicService.getLoopState();
        btnRandom.setImageResource(resIdRandom);
        btnLoop.setImageResource(resIdLoop);
    }


    private void SetTitleForToolbar() {
        tvSongName.setText(songs.get(currentSong).getName());
        tvSingersName.setText(songs.get(currentSong).getAllSingerNames());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


}