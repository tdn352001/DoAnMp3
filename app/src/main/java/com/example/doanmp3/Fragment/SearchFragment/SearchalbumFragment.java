package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AlbumAdapter;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SearchalbumFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    MaterialButton textView;
    AlbumAdapter adapter;
    public static ArrayList<Album> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_searchalbum, container, false);
        AnhXa();
        SetRCV();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_search_album);
        textView = view.findViewById(R.id.txt_search_album_noinfo);
    }

    private void SetRCV() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                recyclerView.removeAllViews();
                adapter = new AlbumAdapter(getContext(), SearchFragment.albums);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                textView.setVisibility(View.INVISIBLE);
            } else
                textView.setVisibility(View.VISIBLE);

        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void SetRv() {
        if (recyclerView != null)
            recyclerView.removeAllViews();

        Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 300);
                if (SearchFragment.albums != null) {
                    arrayList = SearchFragment.albums;
                    if (SearchFragment.albums.size() > 0) {
                        adapter = new AlbumAdapter(getContext(), SearchFragment.albums);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        textView.setVisibility(View.INVISIBLE);
                    } else {
                        textView.setText("Không Tìm Thấy Kết Quả");
                        textView.setVisibility(View.VISIBLE);
                    }
                    handler.removeCallbacks(this);
                }
            }
        };


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                if (recyclerView != null) {
                    recyclerView.removeAllViews();
                    textView.setText("Đang Lấy Dữ Liệu...!");
                    textView.setVisibility(View.VISIBLE);
                    handler.postDelayed(run, 300);
                    handler.removeCallbacks(this);
                }

            }
        };
        handler.postDelayed(runnable, 500);
    }

}

//if (SearchFragment.albums != null) {
//        arrayList = SearchFragment.albums;
//        if (SearchFragment.albums.size() > 0) {
//        adapter = new AlbumAdapter(getContext(), SearchFragment.albums);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        textView.setVisibility(View.INVISIBLE);
//        } else {
//        textView.setText("Không Tìm Thấy Kết Quả");
//        textView.setVisibility(View.VISIBLE);
//        }
//        handler.removeCallbacks(this);
//        } else {
//        textView.setText("Đang Lấy Dữ Liệu...!");
//        textView.setVisibility(View.VISIBLE);
//        }