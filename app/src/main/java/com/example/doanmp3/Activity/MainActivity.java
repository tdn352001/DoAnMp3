package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

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
    public static ArrayList<BaiHat> baiHats;
    public static ArrayList<Playlist> userPlaylist;
    public static ArrayList<ChuDeTheLoai> chudelist, theloailist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = 0;
        AnhXa();
        getUser();
        SetUpViewPager();
        setupBottomNavigation();
        GetUserPlaylist();
        GetBaiHatYeuThich();
        GetBaiHatRecent();
        GetKeyWordRecent();
        GetCategory();
    }



    private void AnhXa() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager_main);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView.getMenu().findItem(R.id.homeFragment).setChecked(true);


    }
    private void getUser() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            user = (User) intent.getSerializableExtra("user");
            DetailUserPlaylistActivity.user = user;
        }

    }
    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
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
               switch (position){
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
    public static void LoadingComplete(){
        progress++;
        if(progress >= 3)
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
                if(userPlaylist == null)
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
                if(UserBaiHatFragment.arrayList == null)
                    UserBaiHatFragment.arrayList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
            }
        });
    }

    public void GetBaiHatRecent(){
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatRecent(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                SearchFragment.baihatrecents = (ArrayList<BaiHat>) response.body();
                baiHats = (ArrayList<BaiHat>) response.body();
                if(SearchFragment.baihatrecents == null){
                    SearchFragment.baihatrecents = new ArrayList<>();
                    baiHats = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    public void GetKeyWordRecent(){
        DataService dataService = APIService.getUserService();
        Call<List<KeyWord>> callback = dataService.GetKeyWordRecent(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<KeyWord>>() {
            @Override
            public void onResponse(Call<List<KeyWord>> call, Response<List<KeyWord>> response) {
                SearchFragment.keyWordArrayList = (ArrayList<KeyWord>) response.body();
                if(SearchFragment.keyWordArrayList == null)
                    SearchFragment.keyWordArrayList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<List<KeyWord>> call, Throwable t) {
            }
        });
    }

    public void GetCategory(){
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
}