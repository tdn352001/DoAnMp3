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

import com.example.doanmp3.NewActivity.SongsListActivity;
import com.example.doanmp3.NewAdapter.ObjectAdapter;
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.NewModel.Object;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlbumFragment extends Fragment {

    View view;
    RecyclerView recyclerView;

    ArrayList<Album> albums;
    ArrayList<Object> objects;
    ObjectAdapter objectAdapter;
    int connectAgainst;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album2, container, false);
        InitControls();
        GetDataAlbum();
        return view;
    }

    private void InitControls() {
        recyclerView = view.findViewById(R.id.rv_album);
        connectAgainst = 0;
    }

    private void GetDataAlbum() {
        NewDataService dataService = APIService.newService();
        Call<List<Album>> callback = dataService.getRandomAlbums();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(@NonNull Call<List<Album>> call, @NonNull Response<List<Album>> response) {
                albums = (ArrayList<Album>) response.body();
                objects = new ArrayList<>();
                if (albums != null) {
                    for (Album album : albums){
                        objects.add(album.convertToObject());
                    }
                }
                SetUpRecycleView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Album>> call, @NonNull Throwable t) {
                if(connectAgainst < 3){
                    GetDataAlbum();
                    connectAgainst++;
                    Log.e("EEE", t.getMessage());
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
        Album album = albums.get(position);
        Intent intent = new Intent(getActivity(), SongsListActivity.class);
        intent.putExtra("album", album);
        startActivity(intent);
    }
}