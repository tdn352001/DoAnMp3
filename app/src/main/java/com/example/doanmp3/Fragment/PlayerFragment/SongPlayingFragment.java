package com.example.doanmp3.Fragment.PlayerFragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class SongPlayingFragment extends Fragment {

    View view;

    CircleImageView songThumbnail;
    TextView songInfo;
    ObjectAnimator objectAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_playing, container, false);
        InitControls();
        SetAnimation();
        return view;
    }

    private void InitControls() {
        songThumbnail = view.findViewById(R.id.thumbnail_song_playing);
        songInfo = view.findViewById(R.id.tv_info_song_playing);
    }


    public void SetSongInfo(Song song) {
        SetThumbnailDiskByBitmap(null, song.getThumbnail());
        SetSongInfo(song.getName(), song.getAllSingerNames());
    }

    public void SetSongInfoSafety(Song song){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(view != null){
                    SetSongInfo(song);
                    handler.removeCallbacks(this);
                }else
                    handler.postDelayed(this, 100);
            }
        }, 10);
    }

    public void SetThumbnailDiskByBitmap(Bitmap bitmap, String link) {
        if (bitmap != null) {
            songThumbnail.setImageBitmap(bitmap);
        } else {
            Glide.with(requireContext()).load(link).into(songThumbnail);
        }
    }

    private void SetAnimation() {
        objectAnimator = ObjectAnimator.ofFloat(songThumbnail, "rotation", 0f, 360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setStartDelay(1);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setupStartValues();
        objectAnimator.start();
    }

    @SuppressLint("SetTextI18n")
    public void SetSongInfo(String songName, String singerName) {
        songInfo.setText(songName + " - " + singerName);
    }

    public void StartOrPauseAnimation(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (resId == R.drawable.ic_play_circle_outline) {
                objectAnimator.pause();
            } else {
                objectAnimator.resume();
            }
        }
    }

}