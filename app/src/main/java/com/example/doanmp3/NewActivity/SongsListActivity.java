package com.example.doanmp3.NewActivity;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doanmp3.NewAdapter.ObjectCircleAdapter;
import com.example.doanmp3.NewAdapter.SongAdapter;
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.NewModel.Object;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.NewModel.Singer;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsListActivity extends AppCompatActivity {

    Toolbar toolbar;
    RoundedImageView imgThumbnail;
    MaterialButton btnPlayRandom;
    RecyclerView rvSong;
    LinearLayout layoutMoreInfo;
    RecyclerView rvSinger;

    ArrayList<Song> songs;
    ArrayList<Object> singerObjects;
    SongAdapter songAdapter;
    ObjectCircleAdapter objectCircleAdapter;
    int connectAgainst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_object);
        GetDataObject();
        InitControls();
        SetupToolBar();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.toolbar_detail_object);
        imgThumbnail = findViewById(R.id.thumbnail_detail_object);
        btnPlayRandom = findViewById(R.id.btn_action_play_random);
        rvSong = findViewById(R.id.rv_detail_object);
        rvSong = findViewById(R.id.rv_detail_object);
        layoutMoreInfo = findViewById(R.id.layout_more_info_object);
        rvSinger = findViewById(R.id.rv_singer);
        connectAgainst = 0;
    }

    private void SetupToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle(R.string.subtitle);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void GetDataObject() {
        singerObjects = new ArrayList<>();

        Intent intent = getIntent();
        if(intent == null){
            return;
        }


        if(intent.hasExtra("album")){
            Album album = intent.getParcelableExtra("album");
            GetSongsFromAlbum(album.getId());
            SetUpUi(album.getThumbnail());
            return;
        }

        Playlist myPlaylist = new Playlist("1", "BlackPink in your area", "https://filenhacmp3.000webhostapp.com/file/6PlaylistBlackPink in your area.jpg");
        GetSongsFromPlaylist(myPlaylist.getId());
        SetUpUi(myPlaylist.getThumbnail());

        if(intent.hasExtra("playlist")){
            Playlist playlist = intent.getParcelableExtra("playlist");
            GetSongsFromPlaylist(playlist.getId());
            SetUpUi(playlist.getThumbnail());
        }
    }

    private void GetSongsFromAlbum(String id) {
        NewDataService dataService = APIService.newService();
        Call<List<Song>> callback = dataService.getSongsFromAlbumId(id);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                GetDataSongFromApi(response);
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                if(connectAgainst < 3){
                    GetSongsFromAlbum(id);
                    connectAgainst++;
                    Log.e("EEE", t.getMessage());
                }
            }
        });
    }

    private void GetSongsFromPlaylist(String id) {
        NewDataService dataService = APIService.newService();
        Call<List<Song>> callback = dataService.getSongsFromPlaylistId(id);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                GetDataSongFromApi(response);
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                if(connectAgainst < 3){
                    GetSongsFromAlbum(id);
                    connectAgainst++;
                    Log.e("EEE", t.getMessage());
                }
            }
        });
    }

    private void GetDataSongFromApi( Response<List<Song>> response){
        songs = (ArrayList<Song>) response.body();
        if(songs == null) { songs = new ArrayList<>();}
        SetUpRecycleViewSong();
        GetInfoSinger();
    }

    private void SetUpRecycleViewSong() {
        songAdapter = new SongAdapter(this, songs, new SongAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {

            }

            @Override
            public void optionClick(int position) {

            }
        });

        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager( new LinearLayoutManager(this, VERTICAL, false));
    }

    private void GetInfoSinger() {
        if(songs == null){
            return;
        }

        for(Song song : songs){
            for(Singer singer : song.getSingers()){
                Object singerObject = singer.convertToObject();
                if(!singerObjects.contains(singerObject)){
                    singerObjects.add(singerObject);
                }
            }
        }
        if(singerObjects.size() > 1){
            layoutMoreInfo.setVisibility(View.VISIBLE);
            SetUpRecycleViewSinger();
        }
    }

    private void SetUpRecycleViewSinger() {
        objectCircleAdapter = new ObjectCircleAdapter(this, singerObjects, position -> {

        });

        rvSinger.setAdapter(objectCircleAdapter);
        rvSinger.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
    }

    private void SetUpUi(String thumbnail) {
        Glide.with(this).asBitmap().load(thumbnail).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgThumbnail.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_song_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}