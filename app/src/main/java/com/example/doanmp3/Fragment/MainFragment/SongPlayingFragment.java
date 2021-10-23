package com.example.doanmp3.Fragment.MainFragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class SongPlayingFragment extends Fragment {

    View view;

    CircleImageView songThumbnail;
    TextView songInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_playing, container, false);
        InitControls();
        return view;
    }

    private void InitControls() {
        songThumbnail = view.findViewById(R.id.thumbnail_song_playing);
        songInfo = view.findViewById(R.id.tv_info_song_playing);
    }


    public void SetSongInfo(Song song){
        SetThumbnailDiskByBitmap(null, song.getThumbnail());
        SetSongInfo(song.getName(), song.getAllSingerNames());
    }

    public void SetThumbnailDiskByBitmap(Bitmap bitmap, String link) {
        if(bitmap != null) {
            songThumbnail.setImageBitmap(bitmap);
        }else{
            Glide.with(requireContext()).load(link).into(songThumbnail);
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetSongInfo(String songName, String singerName){
        songInfo.setText(songName + " - " + singerName);
    }

}