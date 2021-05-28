package com.example.doanmp3.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.AllSongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class UserBaiHatFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    TextView textView;
    AllSongAdapter adapter;
    public static ArrayList<BaiHat> arrayList;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_bai_hat, container, false);
        AnhXa();
        SetupBaiHatYeuThich();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_user_baihat_yeuthich);
        textView = view.findViewById(R.id.txt_user_baihat_yeuthich);
        user = MainActivity.user;
    }

    private void SetupBaiHatYeuThich() {
        arrayList = MainActivity.baihatyeuthichList;
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                textView.setVisibility(View.INVISIBLE);
                SetRecyclerView();
            } else
                textView.setVisibility(View.VISIBLE);
        }
        else
            textView.setVisibility(View.VISIBLE);
    }

    private void SetRecyclerView() {
        adapter = new AllSongAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}