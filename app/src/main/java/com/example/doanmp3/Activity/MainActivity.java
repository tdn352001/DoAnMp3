package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.HomeFragment.HomeFragment;
import com.example.doanmp3.Fragment.SearchFragment.SearchFragment;
import com.example.doanmp3.Fragment.UserFragment.UserBaiHatFragment;
import com.example.doanmp3.Fragment.UserFragment.UserFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.KeyWord;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    public static ProgressBar progressBar;
    public static int progress;
    private long backtime;
    public static User user;
    public static boolean success;
    public static ArrayList<Playlist> userPlaylist;
    public static ArrayList<ChuDeTheLoai> chudelist, theloailist;

    // AppBar Play
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout layoutPlay;
    public static CircleImageView imgBaiHat;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtBaiHat, txtCaSi;
    @SuppressLint("StaticFieldLeak")
    public static ImageView btnStop, btnNext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_mainactivity"));

        progress = 0;
        AnhXa();
        getUser();
        SetUpViewPager();
        setupBottomNavigation();
        GetUserPlaylist();
        GetBaiHatYeuThich();
        GetKeyWordRecent();
        GetCategory();
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

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.searchFragment:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.homeFragment:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.userFragment:
                        viewPager.setCurrentItem(0);
                        break;
                }

                return true;
            }
        });
    }

    private void SetUpViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        List<Fragment> arraylist = new ArrayList<>();
        userFragment = new UserFragment();
        HomeFragment homeFragment = new HomeFragment();
        SearchFragment searchFragment = new SearchFragment();
        arraylist.add(userFragment);
        arraylist.add(homeFragment);
        arraylist.add(searchFragment);
        adapter.setList(arraylist);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.userFragment).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.homeFragment).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.searchFragment).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                userPlaylist = (ArrayList<Playlist>) response.body();
                if (userPlaylist == null)
                    userPlaylist = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

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


    public void GetKeyWordRecent() {
        DataService dataService = APIService.getUserService();
        Call<List<KeyWord>> callback = dataService.GetKeyWordRecent(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<KeyWord>>() {
            @Override
            public void onResponse(Call<List<KeyWord>> call, Response<List<KeyWord>> response) {
                SearchFragment.keyWordArrayList = (ArrayList<KeyWord>) response.body();
                if (SearchFragment.keyWordArrayList == null)
                    SearchFragment.keyWordArrayList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<List<KeyWord>> call, Throwable t) {
            }
        });
    }

    public void GetCategory() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callChuDe = dataService.GetAllChuDe();
        Call<List<ChuDeTheLoai>> callTheLoai = dataService.GetAllTheLoai();
        callChuDe.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                chudelist = (ArrayList<ChuDeTheLoai>) response.body();

                callTheLoai.enqueue(new Callback<List<ChuDeTheLoai>>() {
                    @Override
                    public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                        theloailist = (ArrayList<ChuDeTheLoai>) response.body();
                        SearchFragment.categoryList = chudelist;
                        SearchFragment.categoryList.addAll(theloailist);
                    }

                    @Override
                    public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

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
        Glide.with(getApplicationContext()).load(MusicService.arrayList.get(MusicService.Pos).getHinhBaiHat()).placeholder(R.drawable.song).error(R.drawable.song).into(imgBaiHat);
        txtBaiHat.setText(MusicService.arrayList.get(MusicService.Pos).getTenBaiHat());
        txtCaSi.setText(MusicService.arrayList.get(MusicService.Pos).getTenAllCaSi());
        btnStop.setImageResource(R.drawable.ic_pause);
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
            btnStop.setImageResource(R.drawable.ic_pause);
            if (layoutPlay.getVisibility() != View.VISIBLE)
                AppBarSetVisibility();
        } else {
            btnStop.setImageResource(R.drawable.icon_play);
        }
    }


}