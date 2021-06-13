package com.example.doanmp3.Fragment.UserFragment;

import android.os.Bundle;
import android.os.Handler;
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
    public RecyclerView recyclerView;
    public static TextView textView;
    public static AllSongAdapter adapter;
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
        GetBaiHatYeuThich();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_user_baihat_yeuthich);
        textView = view.findViewById(R.id.txt_user_baihat_yeuthich);
        user = MainActivity.user;

    }

    public void SetupBaiHatYeuThich() {
        SetRecyclerView();
        if (arrayList.size() > 0) {
            textView.setVisibility(View.INVISIBLE);
        } else
            textView.setVisibility(View.VISIBLE);
    }


    private void GetBaiHatYeuThich() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 200);
                if (arrayList != null) {
                    SetupBaiHatYeuThich();
                    handler.removeCallbacks(this);
                }
            }
        };

        handler.postDelayed(runnable, 200);
    }

    public void SetRecyclerView() {
        adapter = new AllSongAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static boolean checkLiked(String IdBaiHat) {

        if (arrayList != null && arrayList.size() > 0)
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getIdBaiHat().equals(IdBaiHat))
                    return true;
            }

        return false;
    }

    public static void BoThichBaiHat(String IdBaiHat) {
        if (arrayList != null && arrayList.size() > 0)
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    arrayList.remove(i);
                    adapter.notifyDataSetChanged();
                }
            }
    }
}