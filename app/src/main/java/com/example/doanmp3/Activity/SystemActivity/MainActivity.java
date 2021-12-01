package com.example.doanmp3.Activity.SystemActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Fragment.MainFragment.HomeFragment;
import com.example.doanmp3.Fragment.SearchFragment.SearchFragment;
import com.example.doanmp3.Fragment.UserFragment.UserFragment;
import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    LinearLayout searchLayout;
    CircleImageView userThumbnail;
    CardView searchView;
    ImageView btnOptions;
    ImageFilterView imgBackground;

    // Layout Control Music
    RelativeLayout layoutControlMusic;
    CircleImageView imgSong;
    TextView tvSong, tvSinger;
    MaterialButton btnPlay, btnNext;
    boolean isLayoutControlVisible;

    //Fragments
    UserFragment userFragment;
    HomeFragment homeFragment;
    public SearchFragment searchFragment;
    ViewPagerAdapter adapter;

    private long backTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_from_service"));
        InitControls();
        InitFragment();
        SetUpBottomNavigation();
        SetUpViewPager();
        HandleEvents();
        StartBoundService();
    }

    private void InitControls() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_main_activity);
        viewPager = findViewById(R.id.view_pager_main_activity);
        imgBackground = findViewById(R.id.img_background);
        searchLayout = findViewById(R.id.layout_search_main_activity);
        userThumbnail = findViewById(R.id.thumbnail_user);
        btnOptions = findViewById(R.id.btn_options_main_activity);
        searchView = findViewById(R.id.search_view);
        layoutControlMusic = findViewById(R.id.appbar_control_music);
        imgSong = findViewById(R.id.img_song_playing);
        tvSong = findViewById(R.id.tv_name_song_playing);
        tvSinger = findViewById(R.id.tv_name_singer_playing);
        btnPlay = findViewById(R.id.btn_pause_appbar);
        btnNext = findViewById(R.id.btn_next_appbar);
    }

    private void InitFragment() {
        userFragment = new UserFragment();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
    }

    @SuppressLint("NonConstantResourceId")
    private void SetUpBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.newsFragment:
                    searchFragment.ClearKeyWord();
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.userFragment:
                    viewPager.setCurrentItem(2);
                    break;
                default:
                    viewPager.setCurrentItem(0);
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetUpViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(searchFragment);
        fragments.add(userFragment);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int id;
                switch (position) {
                    case 1:
                        id = R.id.newsFragment;
                        searchFragment.ClearKeyWord();
                        break;
                    case 2:
                        id = R.id.userFragment;
                        break;
                    default:
                        id = R.id.homeFragment;
                }
                bottomNavigationView.getMenu().findItem(id).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void HandleEvents() {
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnPlay.setOnClickListener(v -> {
            if (isBoundServiceConnected) {
                musicService.ActionPlayOrPause();
                btnPlay.setIconResource(musicService.isMediaPlaying());
            }
        });

        btnNext.setOnClickListener(v -> {
            if (isBoundServiceConnected) {
                musicService.ActionNext();
            }
        });

        layoutControlMusic.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlaySongsActivity.class);
            intent.putExtra("not_start_foreground", true);
            startActivity(intent);
        });
    }

    public void setBackgroundColor(Bitmap bitmap) {
        if (bitmap != null)
            imgBackground.setImageBitmap(bitmap);
    }


    // Listen From Service

    MusicService musicService;
    boolean isBoundServiceConnected;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBoundServiceConnected = true;
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBoundServiceConnected = false;
        }
    };

    private void StartBoundService() {
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("action")) {
                int action = intent.getIntExtra("action", 0);
                HandleActionFromService(action);
            }
        }
    };


    private void HandleActionFromService(int action) {
        if (!isBoundServiceConnected) return;

        switch (action) {
            case MusicService.ACTION_START_PLAY:
                HandleActionStartPlayMusic();
                break;
            case MusicService.ACTION_PLAY_OR_PAUSE:
                HandleActionEventPlayOrPauseMusic();
                break;
            case MusicService.ACTION_CLEAR:
                HandleActionStopService();
                break;
        }
    }

    private void HandleActionStartPlayMusic() {
        if (!isLayoutControlVisible) {
            isLayoutControlVisible = true;
            layoutControlMusic.setVisibility(View.VISIBLE);
        }
        HandleActionEventPlayOrPauseMusic();
        Song currentSong = musicService.getCurrentSong();
        tvSong.setText(currentSong.getName());
        tvSinger.setText(currentSong.getAllSingerNames());
        imgSong.setImageBitmap(musicService.GetBitmapOfCurrentSong());
    }

    private void HandleActionEventPlayOrPauseMusic() {
        btnPlay.setIconResource(musicService.isMediaPlaying());
    }

    private void HandleActionStopService() {
        isLayoutControlVisible = false;
        layoutControlMusic.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (backTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
        }

        backTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

}