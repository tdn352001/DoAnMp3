package com.example.doanmp3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.doanmp3.Model.QuangCao;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.time.temporal.Temporal;
import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {

    Context context;
    ArrayList<QuangCao> arrayList;

    public BannerAdapter(Context context, ArrayList<QuangCao> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater =  LayoutInflater.from(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dong_banner, null);
        ImageView imgBanner = view.findViewById(R.id.imgBanner);
        ImageView imgSong = view.findViewById(R.id.imgBanner2);
        TextView titleSongBanner = view.findViewById(R.id.titleBannerBaiHat);
        TextView contentBanner = view.findViewById(R.id.contentBanner);

        Picasso.with(context).load(arrayList.get(position).getHinhAnh()).into(imgBanner);
        Picasso.with(context).load(arrayList.get(position).getHinhBaiHat()).into(imgSong);
        titleSongBanner.setText(arrayList.get(position).getTenBaiHat());
        contentBanner.setText(arrayList.get(position).getNoiDung());
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
