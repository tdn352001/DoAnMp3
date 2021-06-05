package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Adapter.ViewPagerPlaySongAdapter;
import com.example.doanmp3.Fragment.ListSongFragment;
import com.example.doanmp3.Fragment.PlayFragment;
import com.example.doanmp3.Fragment.SearchFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayNhacActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerPlaySongAdapter ListSongAdapter;
    public static PlayFragment playFragment;
    ListSongFragment listSongFragment;
    SeekBar seekBar;
    TextView txtCurrent, txtTotal;
    ImageButton btnLoop, btnRandom, btnPre, btnNext, btnPlay;
    CircleIndicator indicator;
    SimpleDateFormat simpleDateFormat;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    ArrayList<BaiHat> arrayList;
    ArrayList<Integer> playedlist;
    public boolean isAudio = false;

    int Pos;
    public static boolean repeat = true;
    public static boolean random = false;
    Stack<Integer> stack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        AnhXa();
        CheckRepeatRandom();
        GetIntent();
        eventClick();

    }

    private void CheckRepeatRandom() {
        if (random) {
            btnRandom.setImageResource(R.drawable.random_true);
        } else {
            btnRandom.setImageResource(R.drawable.icon_random);
        }

        if (repeat) {
            btnLoop.setImageResource(R.drawable.ic_loop);
        } else {
            btnLoop.setImageResource(R.drawable.ic_loopone);
        }
    }


    // BẮT SỰ KIỆN CLICK

    private void eventClick() {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play);
                    playFragment.objectAnimator.pause();
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause);
                    playFragment.objectAnimator.resume();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    PlayNhac();
                } else {
                    Pos++;
                    if (Pos > arrayList.size() - 1)
                        Pos = 0;
                    PlayNhac();
                }
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (random) {
                    if (!stack.empty()) {
                        Pos = stack.pop();
                        if (playedlist.size() > 0)
                            playedlist.remove(playedlist.size() - 1);
                    } else
                        Pos = 0;

                    PlayNhac();
                } else {
                    Pos--;
                    if (Pos < 0)
                        Pos = arrayList.size() - 1;
                    PlayNhac();
                }
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (random) {
                    btnRandom.setImageResource(R.drawable.icon_random);
                    random = false;
                } else {
                    btnRandom.setImageResource(R.drawable.random_true);
                    random = true;
                }
            }
        });

        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat) {
                    repeat = false;
                    btnLoop.setImageResource(R.drawable.ic_loopone);
                } else {
                    repeat = true;
                    btnLoop.setImageResource(R.drawable.ic_loop);
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playedlist.add(Pos);
                stack.push(Pos);
                if (arrayList.size() == playedlist.size()) {
                    playedlist.clear();
                }


                if (!repeat) {
                    PlayNhac();
                } else {
                    if (random) {
                        Random rd = new Random();
                        Pos = rd.nextInt(arrayList.size());
                        while (playedlist.contains(Pos))
                            Pos = rd.nextInt(arrayList.size());
                        PlayNhac();
                    } else {
                        Pos++;
                        if (Pos > arrayList.size() - 1)
                            Pos = 0;
                        PlayNhac();
                    }
                }
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
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                txtCurrent.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }


    // LẤY DỮ LIỆU

    private void GetIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra("position"))
            Pos = intent.getIntExtra("position", 0);

        if (intent.hasExtra("mangbaihat")) {
            arrayList = intent.getParcelableArrayListExtra("mangbaihat");
            if (arrayList.size() > 0) {
                listSongFragment = new ListSongFragment(arrayList, mediaPlayer);
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
                PlayNhac();
            }
        }

        if (intent.hasExtra("audio"))
            isAudio = true;


    }

    private void AnhXa() {
//        toolbar = findViewById(R.id.toolbar_play);
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
        playedlist = new ArrayList<>();
        stack = new Stack<>();
    }


    private void TimeSong() {

        txtTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }


    public void PlayNhac() {

        try {
            seekBar.setProgress(0);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrayList.get(Pos).getLinkBaiHat());
            mediaPlayer.prepare();
            mediaPlayer.start();
            UploadToPlayRecent();
            btnPlay.setImageResource(R.drawable.ic_pause);
            SetConTent();
            TimeSong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UploadToPlayRecent() {
        String id = arrayList.get(Pos).getIdBaiHat();
        if (!id.equals("-1")) {
            DataService dataService = APIService.getUserService();
            Call<String> callback = dataService.PlayNhac(MainActivity.user.getIdUser(), id);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = (String) response.body();
                    if (result.equals("S")) {

                        if (!SearchFragment.CheckinSongRecent(arrayList.get(Pos).getIdBaiHat())) {
                            SearchFragment.baihatrecents.add(0, arrayList.get(Pos));
                            Log.e("BBB", SearchFragment.baihatrecents.size() + "");
                            SearchFragment.searchSongAdapter.notifyDataSetChanged();
                        }
                        else{
                            Log.e("BBB", "Da Ton Tai");
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
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
        PlayNhac();
    }


}