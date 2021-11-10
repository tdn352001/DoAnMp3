package com.example.doanmp3.Fragment.NewSearchFragment;

import static com.example.doanmp3.Service.Tools.SetTextStyle;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.NewAdapter.ViewPager2StateAdapter;
import com.example.doanmp3.NewModel.ResultSearch;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchResultFragment extends Fragment {

    //Controls
    View view;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ProgressBar progressBar;
    CoordinatorLayout layoutContainer;

    //Fragment Results
    AllResultFragment allSearchFragment;
    SongResultFragment songResultFragment;
    AlbumResultFragment albumResultFragment;
    SingerResultFragment singerResultFragment;
    PlaylistResultFragment playlistResultFragment;

    // ViewPagerData
    ViewPager2StateAdapter adapter;
    ArrayList<Fragment> fragments;
    ArrayList<String> titleTab;
    int trySearchAgain = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result, container, false);
        InitControls();
        InitFragmentResults();
        SetTitleForTab();
        SetUpViewPagerAndTab();
        HandleEvent();
        GetKeyWord();
        return view;
    }


    private void InitControls() {
        tabLayout = view.findViewById(R.id.tab_layout_result);
        viewPager = view.findViewById(R.id.viewpager_result);
        progressBar = view.findViewById(R.id.progress_bar_load_result);
        layoutContainer = view.findViewById(R.id.result_layout);
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

    private void SetUpViewPagerAndTab() {
        // Setup ViewPager
        adapter = new ViewPager2StateAdapter(this, fragments, titleTab);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        // Set up ViewPager With TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (adapter.getTitles() != null && position < adapter.getTitles().size()) {
                tab.setText(adapter.getTitles().get(position));
            }
        }).attach();

        // Set Text Style For Selected Tab
        int SelectedTabPosition = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tabSelected = tabLayout.getTabAt(SelectedTabPosition);
        if (tabSelected != null) {
            String tabSelectedTitle = Objects.requireNonNull(tabSelected.getText()).toString();
            tabSelected.setText(SetTextStyle(tabSelectedTitle, Typeface.BOLD));
        }
    }

    private void HandleEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabTitle = Objects.requireNonNull(tab.getText()).toString();
                SpannableStringBuilder tabBoldTitle = SetTextStyle(tabTitle, Typeface.BOLD);
                tab.setText(tabBoldTitle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                String tabTitle = Objects.requireNonNull(tab.getText()).toString();
                SpannableStringBuilder tabBoldTitle = SetTextStyle(tabTitle, Typeface.NORMAL);
                tab.setText(tabBoldTitle);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position){
                    case 1:
                        songResultFragment.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        break;
                    case 2:
                        albumResultFragment.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        break;
                    case 3:
                        singerResultFragment.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        break;
                    case 4:
                        playlistResultFragment.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        break;
                    default:
                        allSearchFragment.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            }
        });

        layoutContainer.setOnClickListener(v -> Tools.hideSoftKeyBoard(getActivity()));
    }

    private void GetKeyWord() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String keyWord = bundle.getString("keyWord");
            Search(keyWord);
        }
    }

    private void Search(String keyWord) {
        if(keyWord == null){
            Log.e("ERROR", "can't get key word");
        }
        NewDataService dataService = APIService.newService();
        Call<List<ResultSearch>> callback = dataService.search(keyWord);
        callback.enqueue(new Callback<List<ResultSearch>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResultSearch>> call, @NonNull Response<List<ResultSearch>> response) {
                List<ResultSearch> result = response.body();
                if (result != null) {
                    progressBar.setVisibility(View.GONE);
                    ResultSearch resultSearch = result.get(0);
                    allSearchFragment.DisplayResult(resultSearch, tabLayout);
                    songResultFragment.DisplayResult(resultSearch.getSongs());
                    albumResultFragment.DisplayResult(resultSearch.getAlbums());
                    singerResultFragment.DisplayResult(resultSearch.getSingers());
                    playlistResultFragment.DisplayResult(resultSearch.getPlaylists());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResultSearch>> call, @NonNull Throwable t) {
                Log.e("ERROR", "Search Failed:" + t.getMessage());
                if(trySearchAgain > 3){
                    trySearchAgain = 0;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.search_failed), Toast.LENGTH_SHORT).show();
                }else{
                    Search(keyWord);
                    trySearchAgain++;
                }
            }
        });
    }


}