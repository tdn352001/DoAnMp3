package com.example.doanmp3.Fragment.UserFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AddBaiHatAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class Love_AddFragment extends Fragment {


    View view;
    RecyclerView recyclerView;
    public RelativeLayout textView;
    public AddBaiHatAdapter adapter;
    public ArrayList<BaiHat> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_love__add, container, false);
        AnhXa();
        GetLoveSong();
        return view;
    }

    private void AnhXa() {
        textView = view.findViewById(R.id.txt_noinfo_add_love);
        recyclerView = view.findViewById(R.id.rv_add_love);
    }

    public void GetLoveSong() {
        arrayList = UserBaiHatFragment.arrayList;
        if (arrayList != null) {
            adapter = new AddBaiHatAdapter(getContext(), arrayList, false, true);
            if (arrayList.size() > 0) {
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
}