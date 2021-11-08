package com.example.doanmp3.Fragment.NewSearchFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.SongAdapter;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class SongResultFragment extends Fragment {

    View view;
    RecyclerView rvSong;
    ArrayList<Song> songs;
    SongAdapter adapter;
    LinearLayout layoutNoResult;
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
    }

    private void InitData() {
        songs = new ArrayList<>();
        adapter = new SongAdapter(getActivity(), songs, new SongAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {

            }

            @Override
            public void optionClick(int position) {

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
        if(songsResult == null || songsResult.size() == 0){
            layoutNoResult.setVisibility(View.VISIBLE);
            return;
        }
        songs.clear();
        songs = (ArrayList<Song>) songsResult;
        adapter.setSongs(songs);
    }
}