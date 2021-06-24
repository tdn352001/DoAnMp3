package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchplaylistFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    PlaylistAdapter adapter;
    RelativeLayout layoutNoinfo;
    public ArrayList<Playlist> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_playlist, container, false);
        AnhXa();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_search_playlist);
        layoutNoinfo = view.findViewById(R.id.txt_search_playlist_noinfo);
    }

    public void Search(String query){
        recyclerView.removeAllViews();
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.GetSearchPlaylist(query);
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                arrayList = (ArrayList<Playlist>) response.body();
                SearchFragment.CountResult();
                if (arrayList != null) {
                    if (arrayList.size() > 0) {
                        layoutNoinfo.setVisibility(View.INVISIBLE);
                        SetRecycleView();
                        return;
                    }
                }
                layoutNoinfo.setVisibility(View.VISIBLE);
                SetRecycleView();
            }
            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

    private void SetRecycleView() {
        adapter = new PlaylistAdapter(getContext(), arrayList, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
}