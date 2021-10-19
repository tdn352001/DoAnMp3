package com.example.doanmp3.NewActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySongsActivity extends AppCompatActivity implements ItemClick {

    LinearLayout layoutPlay;
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

    ArrayList<Song> songs;
    int currentSong;

    // Action From Service
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("currentSong")) {
                currentSong = intent.getIntExtra("currentSong", 0);
            }
            if (intent.hasExtra("action")) {
                int action = intent.getIntExtra("action", 0);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);

        InitControls();
        InitViewPagerAdapter();
        SetUpViewPager();
        GetDataSongs();
    }

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
    }

    private void InitViewPagerAdapter() {
        listFragment = new ListSongPlayingFragment();
        songFragment = new SongPlayingFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(listFragment);
        fragments.add(songFragment);
        viewPagerAdapter = new ViewPager2StateAdapter(this, fragments, null);
    }

    private void SetUpViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        circleIndicator3.setViewPager(viewPager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (positionOffset == 0) {
                    return;
                }
                float marginBottom = layoutInteractive.getHeight() * (positionOffset - 1);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, (int) marginBottom);
                layoutInteractive.setLayoutParams(layoutParams);
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);
    }

    private void GetDataSongs() {
        Playlist myPlaylist = new Playlist("1", "BlackPink in your area", "https://filenhacmp3.000webhostapp.com/file/6PlaylistBlackPink in your area.jpg");
        currentSong = 0;
        NewDataService dataService = APIService.newService();
        Call<List<Song>> callback = dataService.getSongsFromPlaylistId(myPlaylist.getId());
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                GetDataSongFromApi(response);
                StartService();
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                GetDataSongs();
            }
        });
    }

    private void GetDataSongFromApi(Response<List<Song>> response) {
        songs = (ArrayList<Song>) response.body();
        if (songs == null) {
            songs = new ArrayList<>();
        }
        listFragment.SetUpRecycleView(songs);
    }



    @Override
    public void onItemClick(int position) {
        Song song = listFragment.getSongs().get(position);
        songFragment.SetThumbnailDiskByBitmap(null, song.getThumbnail());
        songFragment.SetSongInfo(song.getName(), song.getAllSingerNames());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void SetBackground(Song song, int position){
        Bitmap bitmapThumbnail = listFragment.getBitmap(position);
        Bitmap bitmapCropped = Tools.cropBitmap(bitmapThumbnail, 25);
        Bitmap bitmapSaturationChange = Tools.updateHSV(bitmapCropped, 10f, 1f, 0);
        Bitmap bitmapContrastChange = Tools.changeBitmapContrastBrightness(bitmapSaturationChange, 2, -50);
        Bitmap bitmapBlurred = Tools.blurBitmap(PlaySongsActivity.this, bitmapContrastChange, 25f);
        Drawable backgroundDrawables = new BitmapDrawable(getResources(), bitmapBlurred);
        layoutPlay.setBackground(backgroundDrawables);
    }



    private void StartService() {
        Intent MusicService = new Intent(getApplicationContext(), MusicForegroundService.class);
        MusicService.putExtra("songs", songs);
        MusicService.putExtra("currentSong", currentSong);
        startService(MusicService);
    }


}