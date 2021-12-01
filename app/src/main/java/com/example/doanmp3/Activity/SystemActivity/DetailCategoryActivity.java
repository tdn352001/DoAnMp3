package com.example.doanmp3.Activity.SystemActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Adapter.CategoryPlaylistAdapter;
import com.example.doanmp3.Interface.DataService;
import com.example.doanmp3.Models.Genre;
import com.example.doanmp3.Models.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCategoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imgBackground;
    ProgressBar progressBar;
    LinearLayout layoutContent;
    RecyclerView recyclerView;
    TextView tvNoData;

    CategoryPlaylistAdapter adapter;
    ArrayList<Playlist> playlists;
    Genre genre;
    Callback<List<Playlist>> callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);
        InitControls();
        InitVariables();
        GetIntent();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.toolbar);
        imgBackground = findViewById(R.id.img_thumbnails);
        progressBar = findViewById(R.id.progress_bar);
        layoutContent = findViewById(R.id.layout_container);
        recyclerView = findViewById(R.id.rv_song);
        tvNoData = findViewById(R.id.tv_no_data);
    }

    private void InitVariables() {
        callBack = new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                progressBar.setVisibility(View.GONE);
                if(playlists == null || playlists.size() == 0){
                    tvNoData.setVisibility(View.VISIBLE);
                    return;
                }
                layoutContent.setVisibility(View.VISIBLE);
                SetRecyclerView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                tvNoData.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if(intent == null)
            return;

        if(intent.hasExtra("theme")){
            genre = (Genre) intent.getSerializableExtra("theme");
            GetThemePlaylist();
            SetContentView();
            return;
        }

        if(intent.hasExtra("category")){
            genre = (Genre) intent.getSerializableExtra("category");
            GetCategoryPlaylist();
            SetContentView();
        }
    }

    private void SetRecyclerView(){
        adapter = new CategoryPlaylistAdapter(this, playlists, this::NavigateToDetailPlaylist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void SetContentView(){
        Glide.with(this).load(genre.getThumbnail()).into(imgBackground);
        toolbar.setTitle(genre.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void GetThemePlaylist() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getThemePlaylist(genre.getId());
        callback.enqueue(callBack);
    }

    private void GetCategoryPlaylist() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getCategoryPlaylist(genre.getId());
        callback.enqueue(callBack);
    }

    private void NavigateToDetailPlaylist(int position){
        Playlist playlist = playlists.get(position);
        Intent intent = new Intent(this, SongsListActivity.class);
        intent.putExtra("playlist", playlist);
        startActivity(intent);
    }
}