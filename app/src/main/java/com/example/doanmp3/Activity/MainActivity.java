package com.example.doanmp3.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.HomeFragment;
import com.example.doanmp3.Fragment.LibraryFragment;
import com.example.doanmp3.Fragment.SearchFragment;
import com.example.doanmp3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        SetUpViewPager();


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
                    case R.id.libraryFragment:
                        viewPager.setCurrentItem(0);
                        break;
                }

                return true;
            }
        });



    }



    private void AnhXa() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager_main);


        bottomNavigationView.getMenu().findItem(R.id.homeFragment).setChecked(true);

    }

    private void SetUpViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        List<Fragment> arraylist = new ArrayList<>();
        arraylist.add(new LibraryFragment());
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
                       bottomNavigationView.getMenu().findItem(R.id.libraryFragment).setChecked(true);
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
}