package com.example.doanmp3.Fragment.MainFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.UserPlaylistAdapter;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;
import com.example.doanmp3.Interface.OptionItemClick;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserPlaylistFragment extends Fragment {

    View view;
    RelativeLayout btnAdd;
    RecyclerView rvPlaylist;
    ArrayList<Playlist> playlists;
    UserPlaylistAdapter adapter;
    private int connectAgainst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_playlist2, container, false);
        InitComponents();
        HandleEvents();
        GetDataPlaylist();
        return view;
    }

    private void InitComponents() {
        btnAdd = view.findViewById(R.id.btn_add_playlist);
        rvPlaylist = view.findViewById(R.id.rv_user_playlist);
        connectAgainst = 0;
    }

    private void HandleEvents() {
        btnAdd.setOnClickListener(v -> {

        });
    }

    private void GetDataPlaylist() {
        NewDataService dataService = APIService.newService();
        Call<List<Playlist>> callback = dataService.getRandomPlaylists();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                if(playlists == null)
                    playlists = new ArrayList<>();
                SetUpRecycleView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                if(connectAgainst < 3){
                    GetDataPlaylist();
                    connectAgainst++;
                    Log.e("ERROR","GetDataPlaylist " +  t.getMessage());
                }
            }
        });
    }

    private void SetUpRecycleView() {
        adapter = new UserPlaylistAdapter(getContext(), playlists, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onOptionClick(int position) {

            }
        });
        rvPlaylist.setAdapter(adapter);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvPlaylist.setLayoutAnimation(layoutAnimation);
    }
}