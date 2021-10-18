package com.example.doanmp3.Fragment.MainFragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.SongSelectedAdapter;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.ItemClick;

import java.util.ArrayList;

public class ListSongPlayingFragment extends Fragment {

    View view;
    TextView tvSongName, tvSingersName, tvGenres;
    RecyclerView rvSong;

    SongSelectedAdapter songAdapter;
    ArrayList<Song> songs;

    ItemClick itemClick;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_song_playing, container, false);
        InitControls();
        return view;
    }

    private void InitControls() {
        tvSongName = view.findViewById(R.id.tv_name_song);
        tvSingersName = view.findViewById(R.id.tv_singer_of_song);
        tvGenres = view.findViewById(R.id.txt_genre_of_song);
        rvSong = view.findViewById(R.id.rv_list_song);
        itemClick = (ItemClick) getActivity();
    }

    public void SetUpRecycleView(ArrayList<Song> songArrayList) {
        songs = songArrayList;
        songAdapter = new SongSelectedAdapter(getContext(), songs, itemClick, position -> {

        });
        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public Bitmap getBitmap(int position){
        if(songAdapter.getBackgroundDrawables().size() <= position){
            return  BitmapFactory.decodeResource(getResources(), R.drawable.background_gradient);
        }
        return  songAdapter.getBackgroundDrawables().get(position);
    }
}