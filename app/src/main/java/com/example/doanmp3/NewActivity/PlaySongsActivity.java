package com.example.doanmp3.NewActivity;

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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.Fragment.MainFragment.ListSongPlayingFragment;
import com.example.doanmp3.Fragment.MainFragment.SongPlayingFragment;
import com.example.doanmp3.Interface.ItemClick;
import com.example.doanmp3.NewAdapter.ViewPager2StateAdapter;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.MusicForegroundService;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class PlaySongsActivity extends AppCompatActivity implements ItemClick {

    ImageFilterView imgBackground;
    ImageButton btnExit, btnOptions;
    TextView tvSongName, tvSingersName;
    CircleIndicator3 circleIndicator3;
    ViewPager2 viewPager;
    SeekBar timeBar;
    TextView tvCurrentTime, tvTotalTime;
    ImageButton btnRandom, btnPrev, btnPlay, btnNext, btnLoop;
    LinearLayout layoutInteractive;
    MaterialButton btnComments, btnLove;


    ListSongPlayingFragment listFragment;
    SongPlayingFragment songFragment;
    ViewPager2StateAdapter viewPagerAdapter;
    Handler handler;
    SimpleDateFormat simpleDateFormat;
    ArrayList<Song> songs;
    int currentSong;
    int prevSong;
    boolean isRandom;

    // listen from foreground service
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

    // Bind Service
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


    // Update Time In Seekbar
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

    // Firebase
    FirebaseUser user;
    DatabaseReference likeRef;
    ValueEventListener valueEventListener;
    ArrayList<String> likes;

    /* ========= ON CREATE =========*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_from_service"));
        InitControls();
        InitViewPagerAdapter();
        InitFirebase();
        SetUpViewPager();
        GetDataSongs();
        HandleEvent();
    }

    /* ========= InitControls =========*/

    @SuppressLint("SimpleDateFormat")
    private void InitControls() {
        imgBackground = findViewById(R.id.img_background);
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
    /* ========= Hide Layout Like and Comment ===========*/

    private void HideLayoutInteractive(float positionOffset) {
        float marginBottom = layoutInteractive.getHeight() * (positionOffset - 1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, (int) marginBottom);
        layoutInteractive.setLayoutParams(layoutParams);
    }
    /* ========= GetDataSongs =========*/

    private void GetDataSongs() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("songs")) {
                songs = intent.getParcelableArrayListExtra("songs");
                if (songs == null) songs = new ArrayList<>();
            }
            currentSong = intent.getIntExtra("position", 0);
            isRandom = intent.getBooleanExtra("random", false);
            StartForegroundService();
            StartBoundService();
            listFragment.SetUpRecycleView(songs);
            isMusicServiceRunning = true;
        }
    }

    private void InitFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        likeRef = FirebaseDatabase.getInstance().getReference("likes").child("songs");
        likes = new ArrayList<>();
        prevSong = -1;
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

        btnLove.setOnClickListener(v -> HandleEventLoveSong());
        btnComments.setOnClickListener(v -> NavigateToCommentActivity());
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
    private void SetBackground(int position) {
        Bitmap bitmapThumbnail = listFragment.getBitmap(position);
        Bitmap bitmapBlurred = Tools.blurBitmap(PlaySongsActivity.this, bitmapThumbnail, 25f);
        imgBackground.setImageBitmap(bitmapBlurred);
    }

    private void NavigateToCommentActivity() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("type", "songs");
        intent.putExtra("idObject", songs.get(currentSong).getId());
        intent.putExtra("nameObject", songs.get(currentSong).getName());
        startActivity(intent);
    }


    private void ListenLikeEvent() {
        if (prevSong != -1) {
            likeRef.child(songs.get(prevSong).getId()).removeEventListener(valueEventListener);
            valueEventListener = null;
            btnLove.setIconResource(R.drawable.ic_hate);
            btnLove.setText(R.string.like);
        }
        prevSong = currentSong;
        valueEventListener = new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("EEEE", "alo alo");
                if (likes == null)
                    likes = new ArrayList<>();
                else
                    likes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    likes.add(dataSnapshot.getValue(String.class));
                }
                int resDrawableIcon = likes.contains(user.getUid()) ? R.drawable.ic_love : R.drawable.ic_hate;
                btnLove.setIconResource(resDrawableIcon);
                String countLike = likes.size() > 0 ? likes.size() + "" : getString(R.string.like);
                btnLove.setText(countLike);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        likeRef.child(songs.get(currentSong).getId()).addValueEventListener(valueEventListener);
    }

    private void HandleEventLoveSong() {
        if (likes == null) {
            likes = new ArrayList<>();
        }

        if(likes.contains(user.getUid())){
            likes.remove(user.getUid());
        }else
            likes.add(user.getUid());
        likeRef.child(songs.get(currentSong).getId()).setValue(likes);
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
        if (isRandom)
            MusicService.putExtra("random", isRandom);
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

        // Listen Like Change();
        ListenLikeEvent();

        // SetBackgroundColor
        SetBackground(currentSong);

    }

    private void HandleActionEventPlayOrPauseMusic() {
        int resId = musicService.isMediaPlaying();
        btnPlay.setImageResource(resId);
        songFragment.StartOrPauseAnimation(resId);
    }

    private void HandleActionStopService() {
        isMusicServiceRunning = false;
        btnPlay.setImageResource(R.drawable.ic_play_circle_outline);
        songFragment.StartOrPauseAnimation(1);
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
        likeRef.child(songs.get(currentSong).getId()).removeEventListener(valueEventListener);
    }


}