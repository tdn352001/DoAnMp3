package com.example.doanmp3.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doanmp3.Adapter.AllSingerAdapter;
import com.example.doanmp3.Adapter.PlaylistAdapter;
import com.example.doanmp3.Model.CaSi;
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

    RecyclerView recyclerview;
    PlaylistAdapter searchplaylist;
    Toolbar toolbar;
    TextView textViewkhongtimthay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_search_playlist, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewsearchplaylist);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerview.setLayoutManager(layoutManager);
        textViewkhongtimthay=view.findViewById(R.id.textviewkhongcodulieuplaylist);


        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView=(androidx.appcompat.widget.SearchView)menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Searchtukhoa(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void Searchtukhoa(String query){
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback=dataService.GetSearchPlaylist(query);
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> mangplaylist= (ArrayList<Playlist>) response.body();
                if (mangplaylist.size()>0){
                    searchplaylist = new PlaylistAdapter(getActivity(),mangplaylist);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
                    recyclerview.setLayoutManager(gridLayoutManager);
                    recyclerview.setAdapter(searchplaylist);
                    textViewkhongtimthay.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                }else{
                    recyclerview.setVisibility(View.GONE);
                    textViewkhongtimthay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

}