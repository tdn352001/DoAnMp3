package com.example.doanmp3.Fragment.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Activity.PlaySongsActivity;
import com.example.doanmp3.NewAdapter.SongAdapter;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class SongResultFragment extends Fragment {

    View view;
    ArrayList<Song> songs;
    SongAdapter adapter;
    LinearLayout layoutNoResult;
    RecyclerView rvSong;
    NestedScrollView nestedScrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_result, container, false);
        InitControls();
        InitData();
        SetUpRecycleView();
        return view;
    }

    private void InitControls() {
        rvSong = view.findViewById(R.id.rv_search_result_song);
        layoutNoResult = view.findViewById(R.id.layout_no_result_container);
        nestedScrollView = view.findViewById(R.id.song_result_scroll_view);
    }

    private void InitData() {
        songs = new ArrayList<>();
        adapter = new SongAdapter(getActivity(), songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                ArrayList<Song> songArrayList = new ArrayList<>();
                songArrayList.add(songs.get(position));
                Intent intent = new Intent(getContext(), PlaySongsActivity.class);
                intent.putExtra("songs", songArrayList);
                startActivity(intent);
            }

            @Override
            public void onOptionClick(int position) {

            }
        }, (itemView, position) -> {
            LinearLayout itemSong = itemView.findViewById(R.id.layout_item_song);
            int paddingSize = getResources().getDimensionPixelSize(R.dimen._12dp);
            itemSong.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            if(position % 2 == 0){
                itemView.setBackgroundResource(R.color.alabaster);
            }else{
                itemView.setBackgroundResource(R.color.white);
            }
        });

    }

    private void SetUpRecycleView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvSong.setLayoutAnimation(layoutAnimation);
    }

    public void DisplayResult(List<Song> songsResult){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(view != null){
                    if(songsResult == null || songsResult.size() == 0){
                        layoutNoResult.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(songs != null)
                        songs.clear();
                    songs = (ArrayList<Song>) songsResult;
                    adapter.setSongs(songs);
                    handler.removeCallbacks(this);
                }else
                    handler.postDelayed(this, 50);
            }
        }, 50);
    }
}