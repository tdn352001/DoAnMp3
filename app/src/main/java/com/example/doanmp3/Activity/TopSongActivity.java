package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.TopSongAdapter;
import com.example.doanmp3.Interface.DataService;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopSongActivity extends BaseActivity {

    MaterialToolbar toolbar;
    MaterialButton btnPlay;
    RecyclerView recyclerView;

    ArrayList<Song> songs;
    TopSongAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_song);
        InitControls();
        SetToolBar();
        GetTopLoveSongs();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.toolbar_new_songs);
        btnPlay = findViewById(R.id.btn_action_play_random);
        recyclerView = findViewById(R.id.rv_song);
    }

    private void SetRecyclerView() {
        adapter = new TopSongAdapter(this, songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onOptionClick(int position) {

            }
        }, (itemView, position) -> {
            LinearLayout itemSong = itemView.findViewById(R.id.layout_item_song);
            int paddingSize = getResources().getDimensionPixelSize(R.dimen._12dp);
            itemSong.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            if (position % 2 == 0) {
                itemView.setBackgroundResource(R.color.alabaster);
            } else {
                itemView.setBackgroundResource(R.color.white);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    private void SetToolBar() {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void GetTopLoveSongs() {
        DataService dataService = APIService.getService();
        Call<List<Song>> callback = dataService.getTop50LoveSongs();
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                songs = (ArrayList<Song>) response.body();
                SetRecyclerView();
                Log.e("EEE", "CALL API SUCCESS");
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e("EEE", "CALL API FAILED: " + t.getMessage());

            }
        });
    }
}