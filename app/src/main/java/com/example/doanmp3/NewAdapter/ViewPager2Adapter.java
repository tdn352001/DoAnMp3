package com.example.doanmp3.NewAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPager2Adapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragments;
    ArrayList<String> titles;

    public ViewPager2Adapter(@NonNull Fragment fragment, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fragment);
        this.fragments = fragments;
        this.titles = titles;
    }

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fragmentActivity);
        this.fragments = fragments;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }
}
