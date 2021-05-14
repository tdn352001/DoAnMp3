package com.example.doanmp3.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Activity.AllSingerActivity;
import com.example.doanmp3.Adapter.AllSongAdapter;
import com.example.doanmp3.Adapter.SingerAdapter;
import com.example.doanmp3.Adapter.ViewPagerAdapterSearch;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {



    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AllSongAdapter searchAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    TextView textViewkhongcodulieu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        super.onCreate(savedInstanceState);



        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        setHasOptionsMenu(true);

        return view;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterSearch adapter = new ViewPagerAdapterSearch(((AppCompatActivity)getActivity()).getSupportFragmentManager());

        adapter.addFragment(new SearchbaihatFragment(), "Bài hát");
        adapter.addFragment(new SearchalbumFragment(), "Album");
        adapter.addFragment(new SearchcasiFragment(), "Ca sĩ");
        adapter.addFragment(new SearchplaylistFragment(),"Playlist");
        viewPager.setAdapter(adapter);
    }



}