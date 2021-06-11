package com.example.doanmp3.Fragment.UserFragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.AddBaiHatActivity;
import com.example.doanmp3.Adapter.AddBaiHatAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.util.ArrayList;


public class Added_AddFragment extends Fragment {

    View view;
    public RelativeLayout textView;
    RecyclerView recyclerView;
    public static int position;
    public static AddBaiHatAdapter adapter;
    public static ArrayList<BaiHat> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_added__add, container, false);
        AnhXa();
        GetBaiHatAdded();
        return view;
    }


    private void AnhXa() {
        textView = view.findViewById(R.id.txt_noinfo_add_added);
        recyclerView = view.findViewById(R.id.rv_add_added);
    }

    private void GetBaiHatAdded() {
        arrayList = AddBaiHatActivity.arrayList;
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                adapter = new AddBaiHatAdapter(getContext(), arrayList, true);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                textView.setVisibility(View.INVISIBLE);
                return;
            }
        }

        textView.setVisibility(View.VISIBLE);
    }

    public static boolean chechAddedBefore(String IdBaiHat) {
        position = -1;
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++)
                if (arrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    position = i;
                    return true;
                }
        }
        return false;
    }

    public void GetBaiHat() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 200);
                if (AddBaiHatActivity.arrayList != null) {
                    arrayList = AddBaiHatActivity.arrayList;
                    if (arrayList.size() > 0) {
                        adapter = new AddBaiHatAdapter(getContext(), arrayList, true);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        textView.setVisibility(View.INVISIBLE);
                        return;
                    } else
                        textView.setVisibility(View.VISIBLE);

                    handler.removeCallbacks(this);
                }
            }
        };
        handler.postDelayed(runnable, 200);
    }


}