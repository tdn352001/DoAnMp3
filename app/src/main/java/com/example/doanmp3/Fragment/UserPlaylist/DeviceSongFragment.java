package com.example.doanmp3.Fragment.UserPlaylist;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.doanmp3.Models.Audio;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DeviceSongFragment extends Fragment {

    View view;
    RelativeLayout layoutContainer;
    RecyclerView rvSong;
    TextView tvNoData;
    ArrayList<Song> songs;
    ArrayList<Song> addedSong;
    AddSongAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_song, container, false);
        InitControl();
        HandleEvents();
        GetAudioFile();
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

    private void GetAudioFile() {
        songs = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            @SuppressLint("Recycle")
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            Log.e("EEE", "getAudioFiles");
            if (cursor != null && cursor.moveToFirst()) {
                Log.e("EEE", "background");
                do {
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                    Audio audio = new Audio(title, artist, url);
                    Song song = audio.convertToSong();
                    songs.add(song);
                } while (cursor.moveToNext());
                getActivity().runOnUiThread(this::SetUpRecyclerView);
            }
        });
    }

    public void SetUpRecyclerView() {
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
            AddSongUserPlaylistActivity activity = (AddSongUserPlaylistActivity) getActivity();
            activity.UpdateAddedSong(isChecked, song);
        });
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        LayoutAnimationController animLayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(animLayout);
    }

    public void QueryData(String query) {
        adapter.getFilter().filter(query);
    }

    public void setAddedSong(ArrayList<Song> addedSong) {
        this.addedSong = addedSong;
        if (adapter != null)
            adapter.setAddedSongs(addedSong);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void AddOrRemoveAddedSong(boolean isChecked, String uri){
        for(int i = 0; i < songs.size(); i++){
            Song song = songs.get(i);
            if(song.getLink().equals(uri)){
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