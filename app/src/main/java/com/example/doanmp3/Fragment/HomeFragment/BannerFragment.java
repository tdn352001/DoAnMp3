package com.example.doanmp3.Fragment.HomeFragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.BannerAdapter;
import com.example.doanmp3.Model.QuangCao;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerFragment extends Fragment {
    View view;
    ViewPager viewPager;
    CircleIndicator indicator;
    BannerAdapter adapter;
    Handler handler;
    Runnable runnable;
    int CurrentItem;

    public BannerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_banner, container, false);
        AnhXa();
        GetData();
        return view;
    }

    private void AnhXa() {
        viewPager = view.findViewById(R.id.viewpager_banner);
        indicator = view.findViewById(R.id.CircleIndicator);
    }

    private void GetData() {
        DataService dataService= APIService.getService();
        Call<List<QuangCao>> callback =  dataService.GetDataBanner();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                ArrayList<QuangCao>  banners= (ArrayList<QuangCao>) response.body();
                adapter = new BannerAdapter(getActivity(), banners);
                viewPager.setAdapter(adapter);
                indicator.setViewPager(viewPager);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        CurrentItem = viewPager.getCurrentItem();
                        CurrentItem++;
                        if(CurrentItem >= viewPager.getAdapter().getCount())
                            CurrentItem=0;

                        viewPager.setCurrentItem(CurrentItem);
                        handler.postDelayed(runnable, 4000);
                    }
                };

                handler.postDelayed(runnable, 4000);
            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {

            }
        });



        MainActivity.LoadingComplete();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


}