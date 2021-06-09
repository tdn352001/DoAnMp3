package com.example.doanmp3.Fragment.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.AllPlaylistActivity;
import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.PlaylistAdapter;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlaylistFragment extends Fragment {

    View view;
    TextView txt;
    RecyclerView recyclerView;


    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        txt = view.findViewById(R.id.txt_playlist_fragment);
        recyclerView = view.findViewById(R.id.rv_playlist);
        GetData();
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllPlaylistActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.GetRandomPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlists = (ArrayList<Playlist>) response.body();
                PlaylistAdapter adapter = new PlaylistAdapter(getActivity() ,playlists);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                MainActivity.LoadingComplete();

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }
}