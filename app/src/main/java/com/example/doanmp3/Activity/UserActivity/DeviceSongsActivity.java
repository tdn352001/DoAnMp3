package com.example.doanmp3.Activity.UserActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AudioAdapter;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Audio;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceSongsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    NestedScrollView layoutContent;
    MaterialButton btnPlay;
    RecyclerView rvSong;
    TextView tvNoData;
    AudioAdapter adapter;

    ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);
        InitControl();
        SetToolBar();
        getAudioFiles();
    }

    private void InitControl() {
        toolbar = findViewById(R.id.toolbar);
        layoutContent = findViewById(R.id.layout_content);
        btnPlay = findViewById(R.id.btn_action_play_random);
        rvSong = findViewById(R.id.rv_song);
        tvNoData = findViewById(R.id.tv_no_data);
        songs = new ArrayList<>();
    }

    private void SetToolBar() {
        toolbar.setTitle(R.string.on_device);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public void getAudioFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            @SuppressLint("Recycle")
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            Log.e("EEE", "getAudioFiles");
            if (cursor != null && cursor.moveToFirst()) {
                Log.e("EEE", "background");
                do {
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                    Audio audio = new Audio(title, artist, url);
                    Song song = audio.convertToSong();
                    songs.add(song);
                } while (cursor.moveToNext());
                runOnUiThread(this::SetRecyclerView);
            }
        });
    }


    private void SetRecyclerView() {
        adapter = new AudioAdapter(this, songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                Tools.NavigateToPlayActivity(DeviceSongsActivity.this, songs, position, false);
            }

            @Override
            public void onOptionClick(int position) {

            }
        });
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(layoutAnimation);
    }

}