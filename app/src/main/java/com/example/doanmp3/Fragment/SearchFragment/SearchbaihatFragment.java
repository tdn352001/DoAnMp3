package com.example.doanmp3.Fragment.SearchFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.SearchSongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SearchbaihatFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    MaterialButton textView;
    SearchSongAdapter adapter;
    public static ArrayList<BaiHat> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_searchbaihat, container, false);
        AnhXa();
        SetRCV();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_search_baihat);
        textView = view.findViewById(R.id.txt_search_baihat_noinfo);
    }

    private void SetRCV() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                recyclerView.removeAllViews();
                adapter = new SearchSongAdapter(getContext(), arrayList, true);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                textView.setVisibility(View.INVISIBLE);
            } else
                textView.setVisibility(View.VISIBLE);
        } else
            textView.setVisibility(View.VISIBLE);
    }


    public void SetRV() {
        if (recyclerView != null)
            recyclerView.removeAllViews();

        Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 300);
                if (SearchFragment.baiHats != null) {
                    arrayList = SearchFragment.baiHats;
                    if (SearchFragment.baiHats.size() > 0) {
                        adapter = new SearchSongAdapter(getContext(), SearchFragment.baiHats, true);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        textView.setVisibility(View.INVISIBLE);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Không Tìm Thấy Kết Quả");
                    }
                    handler.removeCallbacks(this);
                }
            }
        };


        Runnable runnable = new Runnable() {
            @SuppressLint("SetTextI18n")
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


//                    if (SearchFragment.baiHats != null) {
//                        arrayList = SearchFragment.baiHats;
//                        if (SearchFragment.baiHats.size() > 0) {
//                            adapter = new SearchSongAdapter(getContext(), SearchFragment.baiHats, true);
//                            recyclerView.setAdapter(adapter);
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            textView.setVisibility(View.INVISIBLE);
//                        } else{
//                            textView.setVisibility(View.VISIBLE);
//                            textView.setText("Không Tìm Thấy Kết Quả");
//                        }
//                        handler.removeCallbacks(this);
//                    } else {
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText("Đang Lấy Dữ Liệu...!");
//                    }
