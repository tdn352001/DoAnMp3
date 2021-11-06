package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.UserFragment.UserBaiHatFragment;
import com.example.doanmp3.Fragment.UserFragment.UserFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    UserFragment userFragment;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    public static int progress;
    private long backtime;
    public static User user;
    public static boolean success;
    public static ArrayList<Playlist> userPlaylist;
    public static ArrayList<ChuDeTheLoai> chudelist, theloailist;

    // AppBar Play

    // AppBar Play
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout layoutPlay;
    public static CircleImageView imgBaiHat;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtBaiHat, txtCaSi;
    @SuppressLint("StaticFieldLeak")
    public static MaterialButton btnStop, btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_mainactivity"));

        progress = 0;
        AnhXa();
        getUser();
        GetUserPlaylist();
        GetBaiHatYeuThich();
        AppbarClick();
        if (MusicService.mediaPlayer != null) {
            if (MusicService.mediaPlayer.isPlaying())
                AppBarSetVisibility();
        }
    }


    private void AnhXa() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager_main);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView.getMenu().findItem(R.id.homeFragment).setChecked(true);
        layoutPlay = findViewById(R.id.appbar_play);
        imgBaiHat = findViewById(R.id.img_appbar_play);
        txtBaiHat = findViewById(R.id.txt_tenbaihat_appbar);
        txtCaSi = findViewById(R.id.txt_tencasi_appbar);
        btnNext = findViewById(R.id.btn_next_appbar);
        btnStop = findViewById(R.id.btn_pause_appbar);
    }

    private void getUser() {
        Intent intent = getIntent();
        if (intent.hasExtra("user")) {
            user = (User) intent.getSerializableExtra("user");
            DetailUserPlaylistActivity.user = user;
        }
    }





    public static void LoadingComplete() {
        progress++;
        if (progress >= 3)
            progressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onBackPressed() {
        if (backtime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Nhấn Lần Nữa Để Thoát", Toast.LENGTH_SHORT).show();
        }

        backtime = System.currentTimeMillis();
    }

    private void GetUserPlaylist() {
        DataService dataService = APIService.getUserService();
        Call<List<Playlist>> callback = dataService.GetUserPlaylist(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                userPlaylist = (ArrayList<Playlist>) response.body();
                if (userPlaylist == null)
                    userPlaylist = new ArrayList<>();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {

            }
        });
    }

    private void GetBaiHatYeuThich() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatYeuThich(user.getIdUser());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                UserBaiHatFragment.arrayList = (ArrayList<BaiHat>) response.body();
                if (UserBaiHatFragment.arrayList == null)
                    UserBaiHatFragment.arrayList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
            }
        });
    }





    private void AppbarClick() {
        layoutPlay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayNhacActivity.class);
            intent.putExtra("notstart", 0);
            startActivity(intent);
            overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
        });

        btnStop.setOnClickListener(v -> SendActionToService(MusicService.ACTION_PLAY));

        btnNext.setOnClickListener(v -> SendActionToService(MusicService.ACTION_NEXT));
    }

    private void SendActionToService(int action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("action_activity", action);
        startService(intent);
    }

    public void AppBarSetVisibility() {
        layoutPlay.setVisibility(View.VISIBLE);
        if (MusicService.isAudio)
            imgBaiHat.setImageResource(R.drawable.song);
        else
            Glide.with(MainActivity.this).load(MusicService.arrayList.get(MusicService.Pos).getHinhBaiHat()).placeholder(R.drawable.song).error(R.drawable.song).into(imgBaiHat);
        txtBaiHat.setText(MusicService.arrayList.get(MusicService.Pos).getTenBaiHat());
        txtCaSi.setText(MusicService.arrayList.get(MusicService.Pos).getTenAllCaSi());
        btnStop.setIconResource(R.drawable.ic_pause);
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("pos")) {
                AppBarSetVisibility();
            }
            if (intent.hasExtra("action")) {
                int action = intent.getIntExtra("action", 0);
                ActionFromService(action);
            }
        }
    };

    private void ActionFromService(int action) {
        switch (action) {
            case MusicService.ACTION_START_PLAY:
                AppBarSetVisibility();
                break;
            case MusicService.ACTION_PLAY:
                ActionPlay();
                break;
            case MusicService.ACTION_CLEAR:
                layoutPlay.setVisibility(View.GONE);
                break;
        }
    }

    private void ActionPlay() {
        if (MusicService.mediaPlayer.isPlaying()) {
            btnStop.setIconResource(R.drawable.ic_pause);
            if (layoutPlay.getVisibility() != View.VISIBLE)
                AppBarSetVisibility();
        } else {
            btnStop.setIconResource(R.drawable.icon_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}