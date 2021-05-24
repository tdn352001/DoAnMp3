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
import com.example.doanmp3.Fragment.HomeFragment;
import com.example.doanmp3.Fragment.SearchFragment;
import com.example.doanmp3.Fragment.UserFragment;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    public static ProgressBar progressBar;
    public static int progress;
    private long backtime;
    public static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = 0;
        AnhXa();
        getUser();
        setupBottomNavigation();
        SetUpViewPager();
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
        arraylist.add(new UserFragment());
        arraylist.add(new HomeFragment());
        arraylist.add(new SearchFragment());
        adapter.setList(arraylist);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
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
        if(progress == 6)
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
}