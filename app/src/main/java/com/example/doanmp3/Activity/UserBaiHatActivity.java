package com.example.doanmp3.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.LibraryFragment;
import com.example.doanmp3.Fragment.UserBaiHatFragment;
import com.example.doanmp3.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class UserBaiHatActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bai_hat);
        AnhXa();
        SetupToolBar();
    }




    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_user_baihat);
        tabLayout = findViewById(R.id.tablayout_user_baihat);
        viewPager = findViewById(R.id.viewpager_user_baihat);
        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(new UserBaiHatFragment());
        arrayList.add(new LibraryFragment());
        ArrayList<String> title = new ArrayList<>();
        title.add("Yêu Thích");
        title.add("Trên Thiết Bị");
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.setList(arrayList);
        adapter.setTitle(title);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_song);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_phone);


    }
    private void SetupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}