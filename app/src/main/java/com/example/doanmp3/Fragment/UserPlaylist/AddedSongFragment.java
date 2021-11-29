package com.example.doanmp3.Fragment.UserPlaylist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.AddSongUserPlaylistActivity;
import com.example.doanmp3.Adapter.AddSongAdapter;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;

import java.util.ArrayList;


public class AddedSongFragment extends Fragment {

    View view;
    RelativeLayout layoutContainer;
    RecyclerView rvSong;
    TextView tvNoData;
    ArrayList<Song> songs;
    AddSongAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_added_song, container, false);
        InitControl();
        HandleEvents();
        return view;
    }

    private void InitControl() {
        layoutContainer = view.findViewById(R.id.layout_container);
        rvSong = view.findViewById(R.id.rv_song);
        tvNoData = view.findViewById(R.id.tv_no_data);
    }

    private void HandleEvents() {
        layoutContainer.setOnClickListener(v -> Tools.hideSoftKeyBoard(getActivity()));
    }

    public void SetUpRecyclerView(ArrayList<Song> songArrayList) {
        songs = songArrayList;
        if (songs == null || songs.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
        adapter = new AddSongAdapter(getContext(), songs, true);
        adapter.setSearchCompleted(length -> {
            int visible = length == 0 ? View.VISIBLE : View.GONE;
            tvNoData.setVisibility(visible);
        });
        adapter.setItemChecked((isChecked, song) -> {
            Log.e("EEE", "Added: " + isChecked + " Song: " + song.getName());
            if (!isChecked) {
                AddSongUserPlaylistActivity activity = (AddSongUserPlaylistActivity) getActivity();
                activity.UpdateAddedSong(false, song);
                activity.UpdateAddedSongLoveFragment(isChecked, song.getId());
                activity.UpdateAddedSongOnlineFragment(isChecked, song.getId());
            }
        });
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        LayoutAnimationController animLayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(animLayout);
    }

    public void SetUpRecyclerViewSafety(ArrayList<Song> songArrayList) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tvNoData != null) {
                    SetUpRecyclerView(songArrayList);
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        }, 0);
    }

    public void QueryData(String query) {
        adapter.getFilter().filter(query);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void NotifyDataChange() {
        if (adapter != null && adapter.getItemCount() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else
            tvNoData.setVisibility(View.GONE);

        rvSong.post(() -> adapter.notifyDataSetChanged());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void InsertAddedSong(Song song) {
        if (song == null) {
            return;
        }
        boolean isExist = CheckExitsInAdded(song.getId()) != -1;
        if (!isExist) {
            songs.add(0, song);
            rvSong.post(() -> adapter.notifyDataSetChanged());
            if (tvNoData.getVisibility() == View.VISIBLE) {
                tvNoData.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void RemoveAddedSong(String idSong) {
        int position = CheckExitsInAdded(idSong);
        if (position != -1) {
            songs.remove(position);
            rvSong.post(() -> adapter.notifyDataSetChanged());
            if (songs.size() == 0) {
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    public int CheckExitsInAdded(String idSong) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId().equals(idSong))
                return i;
        }

        return -1;
    }
}