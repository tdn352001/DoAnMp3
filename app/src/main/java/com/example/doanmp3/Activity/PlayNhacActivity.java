package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Adapter.ViewPagerPlaySongAdapter;
import com.example.doanmp3.Fragment.PlayFragment.ListSongFragment;
import com.example.doanmp3.Fragment.PlayFragment.PlayFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class PlayNhacActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerPlaySongAdapter ListSongAdapter;
    @SuppressLint("StaticFieldLeak")
    public static PlayFragment playFragment;
    ListSongFragment listSongFragment;
    SeekBar seekBar;
    TextView txtCurrent, txtTotal;
    ImageButton btnLoop, btnRandom, btnPre, btnNext, btnPlay;
    CircleIndicator indicator;
    SimpleDateFormat simpleDateFormat;
    ArrayList<BaiHat> arrayList;
    int Pos;
    private boolean isRecent;
    public static boolean ServiceIsClear = false;
    public boolean isAudio = false;


    // Action From Service
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("pos")) {
                Pos = intent.getIntExtra("pos", 0);
                SetConTent();
            }
            if (intent.hasExtra("action")) {
                int action = intent.getIntExtra("action", 0);
                ActionFromService(action);
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        overridePendingTransition(R.anim.from_bottom, R.anim.to_top);

        AnhXa();
        GetIntent();
        CheckRepeatRandom();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_activity"));
        eventClick();

    }


    @SuppressLint("SimpleDateFormat")
    private void AnhXa() {
        seekBar = findViewById(R.id.seekbar_time);
        txtCurrent = findViewById(R.id.current_time);
        txtTotal = findViewById(R.id.total_time);
        btnLoop = findViewById(R.id.btn_loop);
        btnNext = findViewById(R.id.btn_next);
        btnPre = findViewById(R.id.btn_prev);
        btnPlay = findViewById(R.id.btn_play);
        btnRandom = findViewById(R.id.btn_random);
        indicator = findViewById(R.id.circle_play);
        simpleDateFormat = new SimpleDateFormat("mm:ss");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ActionFromService(int action) {
        switch (action) {
            case MusicService.ACTION_PLAY:
                ActionPlay();
                break;
            case MusicService.ACTION_CLEAR:
                ActionClear();
                break;
            case MusicService.ACTION_START_PLAY:
                TimeSong();
                ActionPlay();
                break;
            case MusicService.ACTION_PLAY_FALIED:
                ActionPlayFailed();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ActionPlay() {
        if (ServiceIsClear) {
            StartService();
            ActionPlay();
            return;
        }
        if (!MusicService.mediaPlayer.isPlaying()) {
            btnPlay.setImageResource(R.drawable.icon_play);
            PlayFragment.objectAnimator.pause();
            MainActivity.btnStop.setImageResource(R.drawable.icon_play);

        } else {
            btnPlay.setImageResource(R.drawable.ic_pause);
            PlayFragment.objectAnimator.resume();
            MainActivity.btnStop.setImageResource(R.drawable.icon_play);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ActionClear() {
        ServiceIsClear = true;
        btnPlay.setImageResource(R.drawable.icon_play);
        seekBar.setProgress(0);
        txtCurrent.setText(simpleDateFormat.format(0));
        PlayFragment.objectAnimator.pause();
    }

    private void ActionPlayFailed() {
        Toast.makeText(this, "Bài Hát Không Tồn Tại", Toast.LENGTH_SHORT).show();
    }

    private void SendActionToService(int action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("action_activity", action);
        switch (action) {
            case MusicService.ACTION_CHANGE_POS:
                intent.putExtra("pos", Pos);
                break;
        }


        startService(intent);
    }

    // Kiểm tra random
    private void CheckRepeatRandom() {
        if (MusicService.random) {
            btnRandom.setImageResource(R.drawable.random_true);
        } else {
            btnRandom.setImageResource(R.drawable.icon_random);
        }

        if (MusicService.repeat) {
            btnLoop.setImageResource(R.drawable.ic_loop);
        } else {
            btnLoop.setImageResource(R.drawable.ic_loopone);
        }
    }

    // Lấy Dữ Liệu, Danh Sách Bài Hát
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("recent"))
            isRecent = intent.getBooleanExtra("recent", false);

        if (!intent.hasExtra("notstart")) {
            if (intent.hasExtra("position"))
                Pos = intent.getIntExtra("position", 0);

            if (intent.hasExtra("mangbaihat")) {
                arrayList = intent.getParcelableArrayListExtra("mangbaihat");
                if (arrayList.size() > 0) {
                    setViewPager();
                }
            }

            if (intent.hasExtra("audio"))
                isAudio = true;
            StartService();
        } else {
            arrayList = MusicService.arrayList;
            Pos = MusicService.Pos;
            isAudio = MusicService.isAudio;
            if (arrayList != null) {
                setViewPager();
                TimeSong();
                SetConTent();
            }

            try {
                if (MusicService.mediaPlayer != null) {
                    if (MusicService.mediaPlayer.isPlaying()) {
                        btnPlay.setImageResource(R.drawable.ic_pause);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> PlayFragment.objectAnimator.start(), 300);
                    } else {
                        btnPlay.setImageResource(R.drawable.icon_play);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> PlayFragment.objectAnimator.pause(), 300);

                    }
                }
            } catch (Exception e) {
                GetIntent();
            }
        }
    }

    private void setViewPager() {
        listSongFragment = new ListSongFragment(arrayList);
        playFragment = new PlayFragment();
        viewPager = findViewById(R.id.viewpager_play_activity);
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(listSongFragment);
        fragmentArrayList.add(playFragment);
        ListSongAdapter = new ViewPagerPlaySongAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentArrayList);
        viewPager.setAdapter(ListSongAdapter);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(1);
        playFragment = (PlayFragment) ListSongAdapter.getItem(1);
    }

    private void StartService() {
        ServiceIsClear = false;
        Intent MusicService = new Intent(getApplicationContext(), com.example.doanmp3.Service.MusicService.class);
        if (arrayList != null)
            MusicService.putExtra("mangbaihat", arrayList);
        MusicService.putExtra("audio", isAudio);
        MusicService.putExtra("pos", Pos);
        MusicService.putExtra("recent", isRecent);
        startService(MusicService);
        SetConTent();
    }

    // BẮT SỰ KIỆN CLICK

    private void eventClick() {

        btnPlay.setOnClickListener(v -> SendActionToService(MusicService.ACTION_PLAY));

        btnNext.setOnClickListener(v -> SendActionToService(MusicService.ACTION_NEXT));

        btnPre.setOnClickListener(v -> SendActionToService(MusicService.ACTION_PREVIOUS));

        btnRandom.setOnClickListener(v -> {
            if (MusicService.random) {
                btnRandom.setImageResource(R.drawable.icon_random);
                MusicService.random = false;
            } else {
                btnRandom.setImageResource(R.drawable.random_true);
                MusicService.random = true;
            }
        });

        btnLoop.setOnClickListener(v -> {
            if (MusicService.repeat) {
                MusicService.repeat = false;
                btnLoop.setImageResource(R.drawable.ic_loopone);
            } else {
                MusicService.repeat = true;
                btnLoop.setImageResource(R.drawable.ic_loop);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicService.mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (MusicService.mediaPlayer != null) {
                    txtCurrent.setText(simpleDateFormat.format(MusicService.mediaPlayer.getCurrentPosition()));
                    seekBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                } else
                    handler.removeCallbacks(this);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    // LẤY DỮ LIỆU

    private void TimeSong() {
        txtTotal.setText(simpleDateFormat.format(MusicService.mediaPlayer.getDuration()));
        seekBar.setMax(MusicService.mediaPlayer.getDuration());
    }

    public void SetConTent() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ListSongAdapter.getItem(1) != null) {
                    if (arrayList.size() > 0) {
                        playFragment.setHinh(arrayList.get(Pos).getHinhBaiHat(), isAudio);
                        listSongFragment.setInfoBaiHat(arrayList.get(Pos).getTenBaiHat(), arrayList.get(Pos).getTenAllCaSi());
                        playFragment.setContent(arrayList.get(Pos).getTenBaiHat(), arrayList.get(Pos).getTenAllCaSi());
                        listSongFragment.ChuyenBai(Pos);
                        handler.removeCallbacks(this);
                    }
                }
            }
        }, 500);

    }

    public void changePos(int Position) {
        Pos = Position;
        SendActionToService(MusicService.ACTION_CHANGE_POS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_top, R.anim.to_bottom);
    }
}