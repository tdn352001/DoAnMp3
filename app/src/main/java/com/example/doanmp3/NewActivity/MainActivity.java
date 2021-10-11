package com.example.doanmp3.NewActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.Fragment.MainFragment.HomeFragment;
import com.example.doanmp3.Fragment.MainFragment.NewsFragment;
import com.example.doanmp3.Fragment.MainFragment.UserFragment;
import com.example.doanmp3.NewAdapter.ViewPager2Adapter;
import com.example.doanmp3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;
    LinearLayout searchLayout;
    CircleImageView userThumbnail;
    TextInputEditText edtSearch;
    MaterialButton btnOptions;

    //Fragments
    UserFragment userFragment;
    HomeFragment homeFragment;
    NewsFragment newsFragment;
    ViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        InitControls();
        InitFragment();
        SetUpBottomNavigation();
        SetUpViewPager();
    }

    private void InitControls() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_main_activity);
        viewPager = findViewById(R.id.view_pager_main_activity);
        searchLayout = findViewById(R.id.layout_search_main_activity);
        userThumbnail = findViewById(R.id.thumbnail_user);
        edtSearch = findViewById(R.id.edt_search_main_activity);
        btnOptions = findViewById(R.id.btn_options_main_activity);
    }

    private void InitFragment() {
        userFragment = new UserFragment();
        homeFragment = new HomeFragment();
        newsFragment = new NewsFragment();
    }

    @SuppressLint("NonConstantResourceId")
    private void SetUpBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.userFragment:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.newsFragment:
                    viewPager.setCurrentItem(2);
                    break;
                default:
                    viewPager.setCurrentItem(1);
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetUpViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(userFragment);
        fragments.add(homeFragment);
        fragments.add(newsFragment);
        adapter = new ViewPager2Adapter(this, fragments, null);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int id;
                switch (position) {
                    case 0:
                        id = R.id.userFragment;
                        break;
                    case 2:
                        id = R.id.newsFragment;
                        break;
                    default:
                        id = R.id.homeFragment;
                }
                bottomNavigationView.getMenu().findItem(id).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        viewPager.setCurrentItem(1);

    }

}