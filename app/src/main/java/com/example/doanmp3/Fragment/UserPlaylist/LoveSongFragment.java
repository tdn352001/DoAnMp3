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

import com.example.doanmp3.Activity.UserActivity.AddSongUserPlaylistActivity;
import com.example.doanmp3.Adapter.AddSongAdapter;
import com.example.doanmp3.Context.Data.UserData;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class LoveSongFragment extends Fragment {

    View view;
    RelativeLayout layoutContainer;
    RecyclerView rvSong;
    TextView tvNoData;
    ArrayList<Song> songs;
    ArrayList<Song> addedSong;
    AddSongAdapter adapter;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_love_song, container, false);
        InitControl();
        HandleEvents();
        GetLoveSong();
        return view;
    }

    private void InitControl() {
        layoutContainer = view.findViewById(R.id.layout_container);
        rvSong = view.findViewById(R.id.rv_song);
        tvNoData = view.findViewById(R.id.tv_no_data);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    private void HandleEvents(){
        layoutContainer.setOnClickListener(v -> Tools.hideSoftKeyBoard(getActivity()));
    }

    public void SetUpRecyclerView(ArrayList<Song> songArrayList) {
        songs = songArrayList;
        if (songs == null || songs.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        tvNoData.setVisibility(View.GONE);

        adapter = new AddSongAdapter(getContext(), songs);
        adapter.setAddedSongs(addedSong);
        adapter.setSearchCompleted(length -> {
            int visible = length == 0 ? View.VISIBLE : View.GONE;
            tvNoData.setVisibility(visible);
        });
        adapter.setItemChecked((isChecked, song) -> {
            Log.e("EEE", "Added: "+ isChecked + " Song: " + song.getName());
            AddSongUserPlaylistActivity activity = (AddSongUserPlaylistActivity) getActivity();
            activity.UpdateAddedSong(isChecked, song);
            activity.UpdateAddedSongOnlineFragment(isChecked, song.getId());
        });
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        LayoutAnimationController animLayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(animLayout);
    }

    private void GetLoveSong(){
        UserData.GetLoveSongData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!UserData.isLoadingData()) {
                    // create a new list so as not to affect the old list
                    ArrayList<Song> loveSongs =  new ArrayList<>(UserData.getLoveSongs());
                    SetUpRecyclerView(loveSongs);
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 50);
                }
            }
        }, 50);
    }

    public void QueryData(String query){
        adapter.getFilter().filter(query);
    }


    public void setAddedSong(ArrayList<Song> addedSong) {
        this.addedSong = addedSong;
        if(adapter != null)
            adapter.setAddedSongs(addedSong);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void AddOrRemoveAddedSong(boolean isChecked, String idSong){
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).getId().equals(idSong)){
                int position = i;
                rvSong.post(() -> adapter.notifyItemChanged(position));
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void NotifyDataChange() {
        if (adapter != null && adapter.getItemCount() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else
            tvNoData.setVisibility(View.GONE);

        rvSong.post(() -> adapter.notifyDataSetChanged());
    }
}