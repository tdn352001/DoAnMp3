package com.example.doanmp3.Fragment.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.SystemActivity.SongsListActivity;
import com.example.doanmp3.Adapter.ObjectAdapter;
import com.example.doanmp3.Models.Object;
import com.example.doanmp3.Models.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Interface.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlaylistFragment extends Fragment {

    View view;
    RecyclerView recyclerView;

    ArrayList<Playlist> playlists;
    ArrayList<Object> objects;
    ObjectAdapter objectAdapter;
    int connectAgainst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        InitControls();
        GetDataPlaylist();
        return view;
    }

    private void InitControls() {
        recyclerView = view.findViewById(R.id.rv_playlist);
        connectAgainst = 0;
    }

    private void GetDataPlaylist() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getRandomPlaylists();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                objects = new ArrayList<>();
                if (playlists != null) {
                    for (Playlist album : playlists){
                        objects.add(album.convertToObject());
                    }
                }
                SetUpRecycleView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                if(connectAgainst < 3){
                    GetDataPlaylist();
                    connectAgainst++;
                    Log.e("ERROR","GetDataPlaylist "+  t.getMessage());
                }
            }
        });
    }

    private void SetUpRecycleView() {
        objectAdapter = new ObjectAdapter(requireContext(), objects, this::NavigateToDetails);
        recyclerView.setAdapter(objectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }

    private void NavigateToDetails(int position){
        Playlist playlist = playlists.get(position);
        Intent intent = new Intent(getActivity(), SongsListActivity.class);
        intent.putExtra("playlist", playlist);
        startActivity(intent);
    }
}