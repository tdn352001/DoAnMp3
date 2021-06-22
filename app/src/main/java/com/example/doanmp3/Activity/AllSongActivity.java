package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AllSongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<BaiHat> arrayList;

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
            if(arrayList == null)
                return;
            if (arrayList.size() > 0) {
                AllSongAdapter adapter = new AllSongAdapter(AllSongActivity.this, arrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(AllSongActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            } else
                GetData();

        } else
            GetData();
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllSong();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                if(arrayList == null)
                    return;

                DanhSachBaiHatActivity.TenCategoty="Playlist";
                DanhSachBaiHatActivity.TenCategoty = "Bài Hát Được Yêu Thích";
                AllSongAdapter adapter = new AllSongAdapter(AllSongActivity.this, arrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(AllSongActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bài hát được yêu thích");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_allsong);
        recyclerView = findViewById(R.id.rv_allsong);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}