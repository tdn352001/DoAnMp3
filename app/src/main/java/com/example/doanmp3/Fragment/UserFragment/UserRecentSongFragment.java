package com.example.doanmp3.Fragment.UserFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Activity.PlaySongsActivity;
import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class UserRecentSongFragment extends Fragment {

    View view;
    FirebaseUser user;
    DatabaseReference recentSongRef;
    ChildEventListener childEventListener;

    RecyclerView rvSong;
    SongAdapter adapter;
    ArrayList<Song> songs;
    TextView tvNoData;

    public static int RECENT_SONGS_MAX_SIZE = 20;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_user_recent_song, container, false);
        InitControls();
        InitVariables();
        return view;
    }

    private void InitControls() {
        rvSong = view.findViewById(R.id.rv_song);
        tvNoData = view.findViewById(R.id.tv_no_data);
    }

    private void InitVariables() {
        songs = new ArrayList<>();
        adapter = new SongAdapter(getContext(), songs, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onOptionClick(int position) {

            }
        });

        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController animLayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSong.setLayoutAnimation(animLayout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        recentSongRef = FirebaseDatabase.getInstance().getReference("recent_songs").child(user.getUid());
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Song song = snapshot.getValue(Song.class);
                if(song != null){
                    AddRecentSong(song);
                    if(tvNoData.getVisibility() == View.VISIBLE)
                        tvNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        recentSongRef.addChildEventListener(childEventListener);
    }

    private void NavigateToPlayActivity(int position){
        Intent intent = new Intent(getContext(), PlaySongsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("songs", songs);
        startActivity(intent);
    }

    private void AddRecentSong(Song song){
        songs.add(0,song);
        adapter.notifyItemInserted(0);
        adapter.notifyItemRangeChanged(0, songs.size());
        CheckRecentSongsSize();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void CheckRecentSongsSize() {
        if(songs.size() > RECENT_SONGS_MAX_SIZE){
            int lastPosition = songs.size()  -1;
            recentSongRef.child(songs.get(lastPosition).getId()).removeValue();
            songs.remove(lastPosition);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recentSongRef.removeEventListener(childEventListener);
    }
}