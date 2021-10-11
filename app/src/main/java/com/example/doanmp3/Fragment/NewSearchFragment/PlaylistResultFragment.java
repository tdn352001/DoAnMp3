package com.example.doanmp3.Fragment.NewSearchFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.PlaylistAdapter;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistResultFragment extends Fragment {

    View view;
    RecyclerView rvPlaylist;
    ArrayList<Playlist> playlists;
    PlaylistAdapter adapter;

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
    }

    private void InitData() {
        playlists = new ArrayList<>();
        adapter = new PlaylistAdapter(getActivity(), playlists, new PlaylistAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        }, (itemView, position) -> {
            LinearLayout itemPlaylist = itemView.findViewById(R.id.layout_item_playlist);
            int paddingSize = (int) getResources().getDimensionPixelSize(R.dimen._12dp);
            itemPlaylist.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            if(position % 2 != 0){
                itemView.setBackgroundResource(R.color.alabaster);
            }else{
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
        if(playlistsResult == null){
            Log.e("EEE", "Result Null");
            return;
        }
        playlists.clear();
        playlists = (ArrayList<Playlist>) playlistsResult;
        adapter.setPlaylists(playlists);
    }
}