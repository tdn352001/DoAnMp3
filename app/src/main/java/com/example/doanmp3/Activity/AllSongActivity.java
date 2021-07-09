package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<BaiHat> arrayList;
    MaterialButton btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_song);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        init();
        Intent intent = getIntent();
        if (intent.hasExtra("mangbaihat")) {
            arrayList = intent.getParcelableArrayListExtra("mangbaihat");
            if (arrayList == null)
                return;
            if (arrayList.size() > 0) {
                SetRv();
            } else
                GetData();

        } else
            GetData();

        btnPlay.setOnClickListener(v -> {
            if(arrayList != null){
                if(arrayList.size() > 0){
                    Intent intentt = new Intent(this, PlayNhacActivity.class);
                    Random rd = new Random();
                    intentt.putExtra("mangbaihat", arrayList);
                    intentt.putExtra("position", rd.nextInt(arrayList.size()));
                    MusicService.random = true;
                    DanhSachBaiHatActivity.category = "playlist";
                    DanhSachBaiHatActivity.TenCategoty="Ca Khúc Được Yêu Thích Nhiều Nhất";
                    startActivity(intentt);
                }
            }
        });
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllSong();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                if (arrayList == null)
                    return;

                DanhSachBaiHatActivity.TenCategoty = "Playlist";
                DanhSachBaiHatActivity.TenCategoty = "Bảng Xếp Hạng Bài Hát Yêu Thích";
                SetRv();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void SetRv() {
        SongAdapter adapter = new SongAdapter(AllSongActivity.this, arrayList, true);
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        recyclerView.setLayoutAnimation(animlayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllSongActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bài Hát Được Yêu Thích");


        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_allsong);
        recyclerView = findViewById(R.id.rv_allsong);
        btnPlay = findViewById(R.id.btn_action_userplaylist);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}