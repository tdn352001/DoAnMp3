package com.example.doanmp3.Fragment.PlayerFragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.doanmp3.Interface.ItemClick;

import java.util.ArrayList;

public class ListSongPlayingFragment extends Fragment {

    View view;
    TextView tvSongName, tvSingersName, tvGenres;
    RecyclerView rvSong;

    SongSelectedAdapter songAdapter;
    ArrayList<Song> songs;

    ItemClick itemClick;
    boolean isSetUpCompleted;


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
        isSetUpCompleted = true;
    }

    private void SetUpRecycleView(ArrayList<Song> songArrayList){
        songs = songArrayList;
        songAdapter = new SongSelectedAdapter(getContext(), songs, itemClick, position -> {

        });
        if(rvSong == null){
            InitControls();
        }
        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

    }

    public void SetUpRecycleViewSafety(ArrayList<Song> songArrayList) {
        // wait Binding View
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if( isSetUpCompleted){
                    handler.removeCallbacks(this);
                    SetUpRecycleView(songArrayList);
                }else{
                    handler.postDelayed(this, 100);
                }
            }
        }, 0);
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

    @SuppressLint("SetTextI18n")
    public void SetGenre(String category, String genreName){
        tvGenres.setText(category + genreName);
    }

    public void SetSongInfo(String songName, String singersName){
        String SongName = getString(R.string.song_name) +songName;
        String SingersName = getString(R.string.singers_name) + singersName;
        tvSongName.setText(SongName);
        tvSingersName.setText(SingersName);
    }

    public ArrayList<Bitmap> getBitmaps() {
        return songAdapter.getBackgroundDrawables();
    }

    public void ChangeItemSelected(int position){
        songAdapter.changeItemSelected(position);
    }

    public void ChangeInfoSongSelected(ArrayList<Song> songArrayList, int position){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isSetUpCompleted){
                    SetUpRecycleView(songArrayList);
                    SetSongInfo(songArrayList.get(position).getName(), songArrayList.get(position).getAllSingerNames());
                    ChangeItemSelected(position);
                    handler.removeCallbacks(this);
                }else
                    handler.postDelayed(this, 100);
            }
        }, 10);
    }
}