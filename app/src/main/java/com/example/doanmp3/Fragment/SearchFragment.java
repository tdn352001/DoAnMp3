package com.example.doanmp3.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.doanmp3.R;


public class SearchFragment extends Fragment {

    View view;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_search, container, false);
        
        AnhXa();
        setupSearchView();

        searchView.setIconifiedByDefault(false);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("BBB", "close");
                return true;
            }
        });


        return view;
    }

    private void setupSearchView() {
    }

    private void AnhXa() {
        searchView = view.findViewById(R.id.search_view);
    }


}