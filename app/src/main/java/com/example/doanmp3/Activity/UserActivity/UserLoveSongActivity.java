package com.example.doanmp3.Activity.UserActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.SystemActivity.BaseActivity;
import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Context.Data.UserData;
import com.example.doanmp3.Dialog.BottomDialog;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserLoveSongActivity extends BaseActivity {

    MaterialToolbar toolbar;
    NestedScrollView layoutContent;
    MaterialButton btnPlay;
    RecyclerView rvSong;
    TextView tvNoData;

    FirebaseUser user;
    ArrayList<Song> songs;
    SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);
        InitControl();
        SetToolBar();
        GetLoveSongs();
        HandleEvents();
    }



    private void InitControl() {
        toolbar = findViewById(R.id.toolbar);
        layoutContent = findViewById(R.id.layout_content);
        btnPlay = findViewById(R.id.btn_action_play_random);
        rvSong = findViewById(R.id.rv_song);
        tvNoData = findViewById(R.id.tv_no_data);
    }

    private void SetToolBar() {
        toolbar.setTitle(R.string.love_song);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void SetRecyclerView() {
        songAdapter = new SongAdapter(this, songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                Tools.NavigateToPlayActivity(UserLoveSongActivity.this, songs, position, false);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onOptionClick(int position) {
                int oldSize = songs.size();
                BottomDialog dialog = Tools.DialogOptionSongDefault(UserLoveSongActivity.this, songs.get(position));
                if (dialog != null) {
                    dialog.setOnDismissListener(dialog1 -> {
                        if(songs.size() != oldSize){
                            songAdapter.notifyDataSetChanged();
                        }
                    });
                    dialog.show();
                }
            }
        });
        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(layoutAnimation);
    }


    private void GetLoveSongs() {
        UserData.GetLoveSongData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!UserData.isLoadingData()) {
                    songs = UserData.getLoveSongs();
                    if (songs == null || songs.size() == 0) {
                        layoutContent.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                        return;
                    }
                    SetRecyclerView();
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 50);
                }
            }
        }, 50);
    }

    private void HandleEvents() {
        btnPlay.setOnClickListener(v -> {
            if (songs == null || songs.size() == 0) {
                Toast.makeText(UserLoveSongActivity.this, R.string.playlist_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            Tools.NavigateToPlayActivity(UserLoveSongActivity.this, songs, 0, false);
        });
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onRestart() {
        super.onRestart();
        songAdapter.notifyDataSetChanged();
    }
}