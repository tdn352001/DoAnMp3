package com.example.doanmp3.Fragment.HomeFragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.NewAdapter.SlideAdapter;
import com.example.doanmp3.NewModel.Slide;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerFragment extends Fragment {
    View view;
    ViewPager2 sliderViewPager;
    CircleIndicator3 circleIndicatorSlider;
    SlideAdapter slideAdapter;
    ArrayList<Slide> slides;
    Handler handler;
    Runnable runnable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_banner, container, false);
        InitControls();
        InitRunnable();
        GetDataSlide();
        HandleEvent();
        return view;
    }

    private void InitControls() {
        sliderViewPager = view.findViewById(R.id.viewpager_banner);
        circleIndicatorSlider = view.findViewById(R.id.CircleIndicator);
    }

    private void InitRunnable() {

    }

    private void GetDataSlide() {
        NewDataService dataService = APIService.newService();
        Call<List<Slide>> callback = dataService.getSlides();
        callback.enqueue(new Callback<List<Slide>>() {
            @Override
            public void onResponse(@NonNull Call<List<Slide>> call, @NonNull Response<List<Slide>> response) {
                slides = (ArrayList<Slide>) response.body();
                if (slides == null) {
                    slides = new ArrayList<>();
                    Log.e("EEE", "slide null");
                }
                SetSlider();
                Log.e("EEE", slides.get(0).getSong().getName());
            }
            @Override
            public void onFailure(@NonNull Call<List<Slide>> call, @NonNull Throwable t) {

            }
        });
    }

    private void SetSlider() {
        slideAdapter = new SlideAdapter(getContext(), slides, position -> Log.e("EEE", position + ""));
        sliderViewPager.setAdapter(slideAdapter);
        circleIndicatorSlider.setViewPager(sliderViewPager);
    }

    private void HandleEvent() {

    }




}