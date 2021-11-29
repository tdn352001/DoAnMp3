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
import com.example.doanmp3.Activity.SongsListActivity;
import com.example.doanmp3.Adapter.PlaylistAdapter;
import com.example.doanmp3.Models.Playlist;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistResultFragment extends Fragment {

    View view;
    ArrayList<Playlist> playlists;
    PlaylistAdapter adapter;
    LinearLayout layoutNoResult;
    RecyclerView rvPlaylist;
    NestedScrollView nestedScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist_result, container, false);
        InitControls();
        InitData();
        SetUpRecycleView();
        return view;
    }

    private void InitControls() {
        rvPlaylist = view.findViewById(R.id.rv_search_result_playlist);
        layoutNoResult = view.findViewById(R.id.layout_no_result_container);
        nestedScrollView = view.findViewById(R.id.playlist_result_scroll_view);
    }

    private void InitData() {
        playlists = new ArrayList<>();
        adapter = new PlaylistAdapter(getActivity(), playlists, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), SongsListActivity.class);
                intent.putExtra("playlist", playlists.get(position));
                startActivity(intent);
            }

            @Override
            public void onOptionClick(int position) {

            }
        }, (itemView, position) -> {
            LinearLayout itemPlaylist = itemView.findViewById(R.id.layout_item_playlist);
            int paddingSize = getResources().getDimensionPixelSize(R.dimen._12dp);
            itemPlaylist.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            if (position % 2 != 0) {
                itemView.setBackgroundResource(R.color.alabaster);
            } else {
                itemView.setBackgroundResource(R.color.white);
            }

        });
    }

    private void SetUpRecycleView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvPlaylist.setAdapter(adapter);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvPlaylist.setLayoutAnimation(layoutAnimation);
    }

    public void DisplayResult(List<Playlist> playlistsResult){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(view != null){
                    if(playlistsResult == null || playlistsResult.size() == 0){
                        layoutNoResult.setVisibility(View.VISIBLE);
                        return;
                    }
                    playlists.clear();
                    playlists = (ArrayList<Playlist>) playlistsResult;
                    adapter.setPlaylists(playlists);
                }else
                    handler.postDelayed(this, 50);
            }
        }, 50);

    }
}