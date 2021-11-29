package com.example.doanmp3.Fragment.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Activity.SongsListActivity;
import com.example.doanmp3.NewAdapter.AlbumAdapter;
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumResultFragment extends Fragment {


    View view;
    ArrayList<Album> albums;
    AlbumAdapter adapter;
    LinearLayout layoutNoResult;
    RecyclerView rvAlbum;
    public NestedScrollView nestedScrollView;

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
        layoutNoResult = view.findViewById(R.id.layout_no_result_container);
        nestedScrollView = view.findViewById(R.id.album_result_scroll_view);
    }

    private void InitData() {
        albums = new ArrayList<>();
        adapter = new AlbumAdapter(getActivity(), albums, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), SongsListActivity.class);
                intent.putExtra("album", albums.get(position));
                startActivity(intent);
            }

            @Override
            public void onOptionClick(int position) {

            }
        }, (itemView, position) -> {
            LinearLayout itemAlbum = itemView.findViewById(R.id.layout_item_album);
            int paddingSize = getResources().getDimensionPixelSize(R.dimen._12dp);
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(view != null){
                    if(albumsResult == null || albumsResult.size() == 0){
                        layoutNoResult.setVisibility(View.VISIBLE);
                        return;
                    }
                    albums.clear();
                    albums = (ArrayList<Album>) albumsResult;
                    adapter.setAlbums(albums);
                }else
                    handler.postDelayed(this, 50);
            }
        }, 50);

    }
}