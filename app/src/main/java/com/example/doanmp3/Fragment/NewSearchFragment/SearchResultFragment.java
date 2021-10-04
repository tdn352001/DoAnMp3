package com.example.doanmp3.Fragment.NewSearchFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.NewAdapter.ViewPager2Adapter;
import com.example.doanmp3.NewModel.ResultSearch;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchResultFragment extends Fragment {

    //Controls
    View view;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    //Fragment Results
    AllResultFragment allSearchFragment;
    SongResultFragment songResultFragment;
    AlbumResultFragment albumResultFragment;
    SingerResultFragment singerResultFragment;
    PlaylistResultFragment playlistResultFragment;

    // ViewPagerData
    ViewPager2Adapter adapter;
    ArrayList<Fragment> fragments;
    ArrayList<String> titleTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result, container, false);
        InitControls();
        InitFragmentResults();
        SetTitleForTab();
        SetViewPager();
        return view;
    }


    private void InitControls() {
        tabLayout = view.findViewById(R.id.tab_layout_result);
        viewPager = view.findViewById(R.id.viewpager_result);
    }

    private void InitFragmentResults() {
        fragments = new ArrayList<>();
        allSearchFragment = new AllResultFragment();
        songResultFragment = new SongResultFragment();
        albumResultFragment = new AlbumResultFragment();
        singerResultFragment = new SingerResultFragment();
        playlistResultFragment = new PlaylistResultFragment();

        fragments.add(allSearchFragment);
        fragments.add(songResultFragment);
        fragments.add(albumResultFragment);
        fragments.add(singerResultFragment);
        fragments.add(playlistResultFragment);

    }

    private void SetTitleForTab() {
        titleTab = new ArrayList<>();
        titleTab.add(getString(R.string.all));
        titleTab.add(getString(R.string.song));
        titleTab.add(getString(R.string.album));
        titleTab.add(getString(R.string.singer));
        titleTab.add(getString(R.string.playlist));
    }

    private void SetViewPager() {
        adapter = new ViewPager2Adapter(this, fragments, titleTab);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(adapter.getTitles() != null && position < adapter.getTitles().size()){
                tab.setText(adapter.getTitles().get(position));
            }
        }).attach();


    }

    private void Search(String keyWord){
        NewDataService dataService = APIService.newService();

        Call<List<ResultSearch>> callback = dataService.search(keyWord);
        callback.enqueue(new Callback<List<ResultSearch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResultSearch>> call, @NonNull Response<List<ResultSearch>> response) {
                List<ResultSearch> result = response.body();
                ResultSearch resultSearch = result.get(0);
                Log.e("EEEE", resultSearch.getSongs().get(0).getName());
            }

            @Override
            public void onFailure(@NonNull Call<List<ResultSearch>> call, @NonNull Throwable t) {
                Log.e("EEE", "Search Failed:" + t.getMessage());
            }
        });

    }


}