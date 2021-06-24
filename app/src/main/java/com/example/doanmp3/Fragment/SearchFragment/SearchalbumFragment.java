package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AlbumAdapter;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchalbumFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    RelativeLayout layoutNoinfo;
    AlbumAdapter adapter;
    public  ArrayList<Album> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_searchalbum, container, false);
        AnhXa();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_search_album);
        layoutNoinfo = view.findViewById(R.id.txt_search_album_noinfo);
    }

    public void Search(String query) {
        recyclerView.removeAllViews();
        Call<List<Album>> callback = APIService.getService().GetSearchAlbum(query);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                arrayList = (ArrayList<Album>) response.body();
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
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

    }

    private void SetRecycleView() {
        adapter = new AlbumAdapter(getContext(), arrayList, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
}