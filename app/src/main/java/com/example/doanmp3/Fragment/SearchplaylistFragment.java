package com.example.doanmp3.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.PlaylistAdapter;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SearchplaylistFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    public PlaylistAdapter adapter;
    MaterialButton textView;
    public static ArrayList<Playlist> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_playlist, container, false);
        AnhXa();
        SetRCV();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_search_playlist);
        textView = view.findViewById(R.id.txt_search_playlist_noinfo);
    }

    private void SetRCV() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                recyclerView.removeAllViews();
                adapter = new PlaylistAdapter(getContext(), arrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                textView.setVisibility(View.INVISIBLE);
            } else
                textView.setVisibility(View.VISIBLE);
        } else
            textView.setVisibility(View.VISIBLE);
    }

    public void SetRv() {
        if (recyclerView != null)
            recyclerView.removeAllViews();

        Handler handler = new Handler();

        // Kiểm tra Kết quả
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 300);
                if (SearchFragment.playlists != null) {
                    arrayList = SearchFragment.playlists;
                    if (SearchFragment.playlists.size() > 0) {
                        adapter = new PlaylistAdapter(getContext(), SearchFragment.playlists);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        textView.setVisibility(View.INVISIBLE);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Không Tìm Thấy Kết Quả");
                    }
                    handler.removeCallbacks(this);
                }
            }
        };


        // Kiểm tra ánh xạ
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                if (recyclerView != null) {
                    recyclerView.removeAllViews();
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Đang Lấy Dữ Liệu...!");
                    handler.postDelayed(run, 300);
                    handler.removeCallbacks(this);
                }
            }
        };

        handler.postDelayed(runnable, 500);

    }
}


//                    if (SearchFragment.playlists != null) {
//                        arrayList = SearchFragment.playlists;
//                        if (SearchFragment.playlists.size() > 0) {
//                            adapter = new PlaylistAdapter(getContext(), SearchFragment.playlists);
//                            recyclerView.setAdapter(adapter);
//                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//                            textView.setVisibility(View.INVISIBLE);
//                        } else {
//                            textView.setVisibility(View.VISIBLE);
//                            textView.setText("Không Tìm Thấy Kết Quả");
//                        }
//                        handler.removeCallbacks(this);
//                    } else {
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText("Đang Lấy Dữ Liệu...!");
//                    }
