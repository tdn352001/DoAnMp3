package com.example.doanmp3.Activity.SystemActivity;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doanmp3.Adapter.ObjectCircleAdapter;
import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Interface.DataService;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Album;
import com.example.doanmp3.Models.Object;
import com.example.doanmp3.Models.Playlist;
import com.example.doanmp3.Models.Singer;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsListActivity extends BaseActivity {

    Toolbar toolbar;
    ImageFilterView imgBackground;
    RoundedImageView imgThumbnail;
    MaterialButton btnPlayRandom;
    RecyclerView rvSong;
    LinearLayout layoutMoreInfo;
    RecyclerView rvSinger;

    String typeObject;
    String idObject;
    String nameObject;
    ArrayList<Song> songs;
    ArrayList<Object> singerObjects;
    SongAdapter songAdapter;
    ObjectCircleAdapter objectCircleAdapter;
    int connectAgainst;
    boolean isRandom;

    // Firebase
    FirebaseUser user;
    DatabaseReference likeRef;
    ValueEventListener valueEventListener;
    ArrayList<String> likes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_object);
        InitControls();
        GetDataObject();
        HandleEvents();
        InitFirebase();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.toolbar_detail_object);
        imgThumbnail = findViewById(R.id.thumbnail_detail_object);
        imgBackground = findViewById(R.id.img_background);
        btnPlayRandom = findViewById(R.id.btn_action_play_random);
        rvSong = findViewById(R.id.rv_detail_object);
        rvSong = findViewById(R.id.rv_detail_object);
        layoutMoreInfo = findViewById(R.id.layout_more_info_object);
        rvSinger = findViewById(R.id.rv_singer);
        connectAgainst = 0;
    }

    @SuppressLint("ResourceAsColor")
    private void SetupToolBar(String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void HandleEvents() {
        btnPlayRandom.setOnClickListener(v -> {
            Random random = new Random();
            int position = random.nextInt(songs.size());
            isRandom = true;
            NavigateToPlaySongActivity(position);
        });
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
            SetupToolBar(album.getName());
            typeObject= "albums";
            idObject = album.getId();
            nameObject = album.getName();
            return;
        }

        if(intent.hasExtra("playlist")){
            Playlist playlist = intent.getParcelableExtra("playlist");
            GetSongsFromPlaylist(playlist.getId());
            SetUpUi(playlist.getThumbnail());
            SetupToolBar(playlist.getName());
            typeObject= "playlists";
            idObject = playlist.getId();
            nameObject = playlist.getName();
        }
    }

    private void GetSongsFromAlbum(String id) {
        DataService dataService = APIService.getService();
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
                    Log.e("ERROR","GetSongsFromAlbum " + t.getMessage());
                }
            }
        });
    }

    private void GetSongsFromPlaylist(String id) {
        DataService dataService = APIService.getService();
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
                    Log.e("ERROR","GetSongsFromPlaylist " + t.getMessage());
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
        songAdapter = new SongAdapter(this, songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                NavigateToPlaySongActivity(position);
                isRandom = false;
            }

            @Override
            public void onOptionClick(int position) {

            }
        });

        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager( new LinearLayoutManager(this, VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(SongsListActivity.this, R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(layoutAnimation);
    }

    private void GetInfoSinger() {
        if(songs == null){
            return;
        }

        for(Song song : songs){
            for(Singer singer : song.getSingers()){
                Object singerObject = singer.convertToObject();
                if(!Tools.isObjectInObjects(singerObject, singerObjects)){
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
                Bitmap blurBitmap = Tools.blurBitmap(SongsListActivity.this, resource, 25);
                imgBackground.setImageBitmap(blurBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void NavigateToPlaySongActivity(int position){
        Intent intent = new Intent(this, PlaySongsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("songs", songs);
        if(isRandom)
            intent.putExtra("random", isRandom);
        startActivity(intent);
    }

    private void InitFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        likeRef = FirebaseDatabase.getInstance().getReference("likes").child("songs");
        likes = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_song_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.like_object:
                HandleLoveObject(item);
                break;
            case R.id.comment_object:
                NavigateToCommentActivity();
                break;
            case R.id.options_object:
                Toast.makeText(SongsListActivity.this, "options_object", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void HandleLoveObject(MenuItem item) {
        item.setIcon(R.drawable.ic_love);
    }

    private void NavigateToCommentActivity() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("type", typeObject);
        intent.putExtra("idObject", idObject);
        intent.putExtra("nameObject", nameObject);
        startActivity(intent);
    }
}