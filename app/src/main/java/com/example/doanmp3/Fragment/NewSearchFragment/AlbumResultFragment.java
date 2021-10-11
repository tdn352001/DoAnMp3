package com.example.doanmp3.Fragment.NewSearchFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.AlbumAdapter;
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumResultFragment extends Fragment {


    View view;
    RecyclerView rvAlbum;
    ArrayList<Album> albums;
    AlbumAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album_result, container, false);
        InitControls();
        InitData();
        SetUpRecycleView();
        return view;
    }

    private void InitControls() {
        rvAlbum = view.findViewById(R.id.rv_search_result_album);
    }

    private void InitData() {
        albums = new ArrayList<>();
        adapter = new AlbumAdapter(getActivity(), albums, new AlbumAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        }, (itemView, position) -> {
            LinearLayout itemAlbum = itemView.findViewById(R.id.layout_item_album);
            int paddingSize = (int) getResources().getDimensionPixelSize(R.dimen._12dp);
            itemAlbum.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            if(position % 2 != 0){
                itemView.setBackgroundResource(R.color.alabaster);
            }else{
                itemView.setBackgroundResource(R.color.white);
            }

        });

    }

    private void SetUpRecycleView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvAlbum.setAdapter(adapter);
        rvAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvAlbum.setLayoutAnimation(layoutAnimation);
    }

    public void DisplayResult(List<Album> albumsResult){
        if(albumsResult == null){
            Log.e("EEE", "Result Null");
            return;
        }
        albums.clear();
        albums = (ArrayList<Album>) albumsResult;
        adapter.setAlbums(albums);
    }
}