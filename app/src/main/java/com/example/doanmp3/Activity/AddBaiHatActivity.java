package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.UserFragment.Added_AddFragment;
import com.example.doanmp3.Fragment.UserFragment.Love_AddFragment;
import com.example.doanmp3.Fragment.UserFragment.Online_AddFragment;
import com.example.doanmp3.Fragment.UserFragment.Recent_AddFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBaiHatActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    //Fragment in View Pager;
    Added_AddFragment addedFragment;
    Love_AddFragment loveFragment;
    Online_AddFragment onlineFragment;
    Recent_AddFragment recentFragment;

    //DetailPlaylist
    String IdPlaylist, TenPlaylist;
    public static ArrayList<BaiHat> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bai_hat);
        AnhXa();
        GetInfoPlaylist();
        SetToolBar();
        SetViewPager();
        SetUpSearchView();
    }

    private void GetInfoPlaylist() {
        Intent intent = getIntent();
        IdPlaylist = intent.getStringExtra("idplaylist");
        TenPlaylist = intent.getStringExtra("tenplaylist");
        arrayList = DetailUserPlaylistActivity.arrayList;
//        IdPlaylist = "35";
//        TenPlaylist = "Test Thôi Nhen";
//        GetBaiHatPlaylist();
    }


    private void AnhXa() {
        tabLayout = findViewById(R.id.tablayout_addbaihat);
        toolbar = findViewById(R.id.toolbar_addbaihat);
        viewPager = findViewById(R.id.viewpager_addbaihat);
        searchView = findViewById(R.id.search_view_addbaihat);
    }

    private void SetToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TenPlaylist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void SetViewPager() {
        // List Fragment
        addedFragment = new Added_AddFragment();
        loveFragment = new Love_AddFragment();
        onlineFragment = new Online_AddFragment();
        recentFragment = new Recent_AddFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(addedFragment);
        fragmentList.add(onlineFragment);
        fragmentList.add(loveFragment);
        fragmentList.add(recentFragment);

        // List Title
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Đã Thêm");
        titles.add("Online");
        titles.add("Yêu Thích");
        titles.add("Gần Đây");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.setList(fragmentList);
        viewPagerAdapter.setTitle(titles);
        viewPager.setCurrentItem(2);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void SetUpSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    Added_AddFragment.adapter.getFilter().filter(query);
                    SearchOnline(query);
                    loveFragment.adapter.getFilter().filter(query);
                    recentFragment.adapter.getFilter().filter(query);

                    if (Added_AddFragment.adapter.getItemCount() == 0) {
                        addedFragment.textView.setVisibility(View.VISIBLE);
                    } else addedFragment.textView.setVisibility(View.GONE);

                    if (loveFragment.adapter.getItemCount() == 0) {
                        loveFragment.textView.setVisibility(View.VISIBLE);
                    } else loveFragment.textView.setVisibility(View.GONE);

                    if (recentFragment.adapter.getItemCount() == 0) {
                        recentFragment.textView.setVisibility(View.VISIBLE);
                    } else recentFragment.textView.setVisibility(View.GONE);


                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    onlineFragment.SearchArrayList = onlineFragment.arrayList;
                    onlineFragment.SetResultBaiHat();
                } else
                    SearchOnline(newText);

                Added_AddFragment.adapter.getFilter().filter(newText);
                if (Added_AddFragment.adapter.getItemCount() == 0) {
                    addedFragment.textView.setVisibility(View.VISIBLE);
                } else
                    addedFragment.textView.setVisibility(View.GONE);

                loveFragment.adapter.getFilter().filter(newText);
                recentFragment.adapter.getFilter().filter(newText);

                if (loveFragment.textView.getVisibility() == View.VISIBLE)
                    loveFragment.textView.setVisibility(View.GONE);
                if (recentFragment.textView.getVisibility() == View.VISIBLE)
                    recentFragment.textView.setVisibility(View.GONE);

                return true;
            }
        });
    }

    private void SearchOnline(String query) {
        onlineFragment.SearchArrayList = null;
        Call<List<BaiHat>> callback = APIService.getService().GetSearchBaiHat(query);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                onlineFragment.SearchArrayList = (ArrayList<BaiHat>) response.body();
                if (onlineFragment.SearchArrayList == null) {
                    onlineFragment.SearchArrayList = new ArrayList<>();
                }
                onlineFragment.SetResultBaiHat();

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
            }
        });
    }

//    private void GetBaiHatPlaylist() {
//        DataService dataService = APIService.getService();
//        Call<List<BaiHat>> callback = dataService.GetUserBaiHatPlaylist("22", IdPlaylist);
//        callback.enqueue(new Callback<List<BaiHat>>() {
//            @Override
//            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
//                arrayList = (ArrayList<BaiHat>) response.body();
//
//                if (arrayList == null) {
//                    arrayList = new ArrayList<>();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
//            }
//        });
//    }

}