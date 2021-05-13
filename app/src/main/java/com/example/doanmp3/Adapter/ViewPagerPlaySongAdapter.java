package com.example.doanmp3.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerPlaySongAdapter extends FragmentPagerAdapter {
    public ViewPagerPlaySongAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> arrayList) {
        super(fm, behavior);
        this.arrayList = arrayList;
    }

    List<Fragment> arrayList;
    public ViewPagerPlaySongAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<Fragment> arrayList) {
        this.arrayList = arrayList;
    }
}
