package com.example.doanmp3.NewActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.NewAdapter.ObjectAdapter;
import com.example.doanmp3.NewAdapter.SongAdapter;
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.NewModel.DetailSinger;
import com.example.doanmp3.NewModel.Object;
import com.example.doanmp3.NewModel.Singer;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingerActivity extends BaseActivity {

    // Controls
    Toolbar toolbar;
    LinearLayout layoutContainer,layoutAlbum;
    ImageView imgSinger;
    RecyclerView rvSong, rvAlbum;
    ProgressBar progressBar;
    MaterialButton btnViewMore;

    // Data
    Singer singer;
    ArrayList<Song> songs;
    ArrayList<Album> albums;
    ArrayList<Object> objects;

    // Adapter
    SongAdapter songAdapter;
    ObjectAdapter objectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);
        InitControls();
        SetUpToolBar();
        GetIntent();
        HandleEvents();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.toolbar_singer);
        layoutContainer = findViewById(R.id.layout_singer_container);
        layoutAlbum = findViewById(R.id.layout_album);
        imgSinger = findViewById(R.id.img_thumbnails_singer);
        rvSong = findViewById(R.id.rv_song);
        rvAlbum = findViewById(R.id.rv_album);
        progressBar = findViewById(R.id.progress_load_detail_singer);
        btnViewMore = findViewById(R.id.btn_view_more);

        layoutContainer.setVisibility(View.GONE);
    }

    private void SetUpToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("singer")){
            singer = intent.getParcelableExtra("singer");
            if(singer == null)
                return;

            toolbar.setTitle(singer.getName());
            Glide.with(SingerActivity.this).load(singer.getThumbnail()).into(imgSinger);
            GetDetailsSinger();
        }
    }

    private void GetDetailsSinger() {
        NewDataService dataService = APIService.newService();

        Call<DetailSinger> callback = dataService.getDetailsSinger(singer.getId());
        callback.enqueue(new Callback<DetailSinger>() {
            @Override
            public void onResponse(@NonNull Call<DetailSinger> call, @NonNull Response<DetailSinger> response) {
                DetailSinger detailSinger = response.body();
                if(detailSinger != null){
                    songs = (ArrayList<Song>) detailSinger.getSongs();
                    albums = (ArrayList<Album>) detailSinger.getAlbums();
                    objects = (ArrayList<Object>) detailSinger.getObjectAlbums();
                    layoutContainer.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    SetUpRvSong();
                    SetUpRvAlbum();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailSinger> call, @NonNull Throwable t) {
                Log.e("ERROR:", t.getMessage());
                Toast.makeText(SingerActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void SetUpRvSong(){
        if(songs == null) return;
        if(songs.size() < 7){
            btnViewMore.setVisibility(View.GONE);
        }

        songAdapter = new SongAdapter(SingerActivity.this, songs, new SongAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                NavigateToPlayActivity(position);
            }

            @Override
            public void optionClick(int position) {

            }
        }, (itemView, position) -> {
            RoundedImageView imgThumbnail = itemView.findViewById(R.id.thumbnail_item_song);
            imgThumbnail.setCornerRadius(Tools.ConvertDpToPx(8, getApplicationContext()));
        });
        songAdapter.setViewMore(true);
        songAdapter.setQuantityItemDisplay(5);
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSong.setLayoutAnimation(layoutAnimation);
    }

    private void SetUpRvAlbum(){
        if(objects == null || objects.size() == 0) {
            layoutAlbum.setVisibility(View.GONE);
            return;
        }
        objectAdapter = new ObjectAdapter(SingerActivity.this, objects, this::NavigateToDetailsAlbum);
        rvAlbum.setAdapter(objectAdapter);
        rvAlbum.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void NavigateToPlayActivity(int position){
        Intent intent = new Intent(SingerActivity.this, PlaySongsActivity.class);
        intent.putExtra("songs", songs);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void NavigateToDetailsAlbum(int position){
        Album album = albums.get(position);
        Intent intent = new Intent(this, SongsListActivity.class);
        intent.putExtra("album", album);
        startActivity(intent);
    }


    private void HandleEvents() {
        btnViewMore.setOnClickListener(v -> {
            boolean isViewMore = songAdapter.isViewMore();
            String viewMore = isViewMore ? getString(R.string.view_less) : getString(R.string.xem_them);
            btnViewMore.setText(viewMore);
            songAdapter.setViewMore(!isViewMore);
        });
    }
}