package com.example.doanmp3.Activity.UserActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doanmp3.Activity.SystemActivity.BaseActivity;
import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Dialog.BottomDialog;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.Models.UserPlaylist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class DetailUserPlaylistActivity extends BaseActivity {

    ImageFilterView imgBackground;
    Toolbar toolbar;
    RoundedImageView imgPlaylist;
    MaterialButton btnPlay;
    LinearLayout btnAddSong, layoutNoInfo;
    RecyclerView rvSong;

    UserPlaylist playlist;
    ArrayList<Song> songs;
    SongAdapter songAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_playlist);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        InitControls();
        GetIntent();
        HandleEvents();
    }

    private void InitControls() {
        imgBackground = findViewById(R.id.img_background_playlist);
        toolbar = findViewById(R.id.toolbar_detail_object);
        imgPlaylist = findViewById(R.id.thumbnail_detail_object);
        btnPlay = findViewById(R.id.btn_action_play_random);
        btnAddSong = findViewById(R.id.btn_add_song);
        layoutNoInfo = findViewById(R.id.layout_no_info);
        rvSong = findViewById(R.id.rv_song);
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("playlist")) {
            playlist = intent.getParcelableExtra("playlist");
            if (playlist != null) {
                GetSongsOfPlaylist();
                SetView();
                SetToolBar();
            }
        }
    }

    private void GetSongsOfPlaylist() {
        songs = playlist.getSongs();
        if (songs == null || songs.size() == 0) {
            songs = new ArrayList<>();
            layoutNoInfo.setVisibility(View.VISIBLE);
        }
        SetUpRecyclerView();
    }

    private void SetView() {
        Glide.with(this).asBitmap().load(playlist.getThumbnail()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgPlaylist.setImageBitmap(resource);
                Bitmap blurBitmap = Tools.blurBitmap(DetailUserPlaylistActivity.this, resource, 25);
                imgBackground.setImageBitmap(blurBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                imgPlaylist.setImageResource(R.drawable.playlist_viewholder);
            }
        });
    }

    private void SetToolBar() {
        toolbar.setTitle(playlist.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    private void SetUpRecyclerView() {
        songAdapter = new SongAdapter(this, songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                Tools.NavigateToPlayActivity(DetailUserPlaylistActivity.this, songs, position, false);
            }

            @Override
            public void onOptionClick(int position) {
                BottomDialog dialog = Tools.DialogOptionSongDefault(DetailUserPlaylistActivity.this, songs.get(position));
                if(dialog != null) dialog.show();
            }
        });
        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(layoutAnimation);
    }

    private void HandleEvents() {
        btnPlay.setOnClickListener(v -> {
            if(songs == null && songs.size() == 0){
                Toast.makeText(DetailUserPlaylistActivity.this, R.string.playlist_empty, Toast.LENGTH_SHORT).show();
            }else{
                Tools.NavigateToPlayActivity(DetailUserPlaylistActivity.this, songs, 0, false);
            }
        });

        btnAddSong.setOnClickListener(v -> {
            Intent intent = new Intent(DetailUserPlaylistActivity.this, AddSongUserPlaylistActivity.class);
            intent.putExtra("playlist", playlist);
            addSongResult.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> addSongResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK){
            Intent intent = result.getData();
            if(intent.hasExtra("songs")){
                songs = intent.getParcelableArrayListExtra("songs");
                playlist.setSongs(songs);
                if (songs == null || songs.size() == 0) {
                    layoutNoInfo.setVisibility(View.VISIBLE);
                    return;
                }
                layoutNoInfo.setVisibility(View.GONE);
                SetUpRecyclerView();
            }else{
                Log.e("EEE", "Không có data");
            }
        }
    });

}