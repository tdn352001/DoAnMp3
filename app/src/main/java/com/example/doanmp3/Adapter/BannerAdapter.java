package com.example.doanmp3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.QuangCao;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerAdapter extends PagerAdapter {

    Context context;
    ArrayList<QuangCao> arrayList;
    ArrayList<BaiHat> arrayListbaihat;

    public BannerAdapter(Context context, ArrayList<QuangCao> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        DataService dataService= APIService.getService();
        Call<List<BaiHat>> callbaihatquangcao = dataService.GetBaiHatQuangCao();
        callbaihatquangcao.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> baiHats = (ArrayList<BaiHat>) response.body();
                arrayListbaihat = baiHats;
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
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



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayNhacActivity.class);
                intent.putExtra("mangbaihat", arrayListbaihat);
                intent.putExtra("position", position);
                DanhSachBaiHatActivity.category="Playlist";
                DanhSachBaiHatActivity.TenCategoty ="Bài hát mới";
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
