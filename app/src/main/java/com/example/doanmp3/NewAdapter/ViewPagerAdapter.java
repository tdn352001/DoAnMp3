package com.example.doanmp3.NewAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list;
    ArrayList<String> title;


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> list) {
        super(fm, behavior);
        this.list = list;
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> list, ArrayList<String> title) {
        super(fm, behavior);
        this.list = list;
        this.title = title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }
}
