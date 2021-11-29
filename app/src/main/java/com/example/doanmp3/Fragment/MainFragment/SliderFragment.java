package com.example.doanmp3.Fragment.MainFragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.Animation.ZoomOutPageTransformer;
import com.example.doanmp3.Activity.NewSongsActivity;
import com.example.doanmp3.Activity.PlaySongsActivity;
import com.example.doanmp3.NewAdapter.SlideAdapter;
import com.example.doanmp3.NewModel.Slide;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.example.doanmp3.Service.Tools;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SliderFragment extends Fragment implements View.OnClickListener {

    View view;
    RelativeLayout layoutSlider;
    LinearLayout layoutNavigation, btnNewSongs, btnCategories, btnTop, btnMV, btnVIP;
    ViewPager2 sliderViewPager;
    CircleIndicator3 circleIndicatorSlider;
    SlideAdapter slideAdapter;
    ArrayList<Slide> slides;
    ArrayList<Song> songs;
    Handler handler;
    Runnable runnable;
    int connectAgainst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slider, container, false);
        InitControls();
        InitRunnable();
        GetDataSlide();
        HandleEvent();
        return view;
    }

    private void InitControls() {
        connectAgainst = 0;
        sliderViewPager = view.findViewById(R.id.view_pager_slider);
        circleIndicatorSlider = view.findViewById(R.id.circle_indicator3);
        layoutSlider = view.findViewById(R.id.layout_slider);
        layoutNavigation = view.findViewById(R.id.layout_nav);
        btnNewSongs = view.findViewById(R.id.btn_new_songs);
        btnCategories = view.findViewById(R.id.btn_categories);
        btnTop = view.findViewById(R.id.btn_top);
        btnMV = view.findViewById(R.id.btn_mv);
        btnVIP = view.findViewById(R.id.btn_vip);
        songs = new ArrayList<>();
    }

    private void InitRunnable() {
        handler = new Handler();
        runnable = () -> {
            if (slides == null)
                return;
            handler.removeCallbacks(runnable);
            int currentItem = sliderViewPager.getCurrentItem();
            currentItem++;
            if (currentItem == slides.size()) {
                currentItem = 0;
            }
            sliderViewPager.setCurrentItem(currentItem);
        };
        handler.postDelayed(runnable, 5000);
    }

    private void GetDataSlide() {

        DataService dataService = APIService.getService();
        Call<List<Slide>> callback = dataService.getSlides();
        callback.enqueue(new Callback<List<Slide>>() {
            @Override
            public void onResponse(@NonNull Call<List<Slide>> call, @NonNull Response<List<Slide>> response) {
                slides = (ArrayList<Slide>) response.body();
                if (slides == null) {
                    slides = new ArrayList<>();
                }
                GetSongFromSlider();
                SetSlider();
            }

            @Override
            public void onFailure(@NonNull Call<List<Slide>> call, @NonNull Throwable t) {
                if (connectAgainst < 3) {
                    GetDataSlide();
                    connectAgainst++;
                }
            }
        });
    }

    private void GetSongFromSlider() {
        for (int i = 0; i < slides.size(); i++) {
            songs.add(slides.get(i).getSong());

        }
    }

    private void SetSlider() {
        slideAdapter = new SlideAdapter(getContext(), slides, this::NavigateToPlayActivity);
        sliderViewPager.setAdapter(slideAdapter);
        circleIndicatorSlider.setViewPager(sliderViewPager);
        sliderViewPager.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void HandleEvent() {
        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.postDelayed(runnable, 5000);
                SetLayoutBackground(position);
            }
        });

        btnNewSongs.setOnClickListener(v -> NavigateToDetail(NewSongsActivity.class));
        btnMV.setOnClickListener(this);
        btnCategories.setOnClickListener(this);
        btnTop.setOnClickListener(this);
        btnVIP.setOnClickListener(this);
    }

    private void NavigateToPlayActivity(int position) {
        Intent intent = new Intent(getActivity(), PlaySongsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("songs", songs);
        startActivity(intent);
    }

    private void NavigateToDetail(Class<?> destination) {
        Intent intent = new Intent(getActivity(), destination);
        startActivity(intent);
    }


    private void SetLayoutBackground(int position) {
        GradientDrawable backgroundDrawables = slideAdapter.getBackgroundDrawables(position);
        if (backgroundDrawables != null) {
            layoutSlider.setBackground(backgroundDrawables);
        }

        layoutNavigation.setBackgroundColor(slideAdapter.getNavColors(position));
    }


    @Override
    public void onClick(View v) {
        Tools.FeatureIsImproving(getActivity());
    }
}