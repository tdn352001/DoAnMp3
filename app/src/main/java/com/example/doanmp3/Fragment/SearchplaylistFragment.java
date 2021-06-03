package com.example.doanmp3.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.PlaylistAdapter;
import com.example.doanmp3.R;

public class SearchplaylistFragment extends Fragment {

    RecyclerView recyclerview;
    PlaylistAdapter searchplaylist;
    Toolbar toolbar;
    TextView textViewkhongtimthay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_search_playlist, container, false);



        return view;
    }



}