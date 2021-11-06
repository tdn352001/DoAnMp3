package com.example.doanmp3.Fragment.UserFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AddBaiHatAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class Recent_AddFragment extends Fragment {

    View view;
    public RelativeLayout textView;
    RecyclerView recyclerView;
    public AddBaiHatAdapter adapter;
    public ArrayList<BaiHat> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent__add, container, false);
        AnhXa();
        GetRecentSong();
        return view;
    }

    private void AnhXa() {
        textView = view.findViewById(R.id.txt_noinfo_add_recent);
        recyclerView = view.findViewById(R.id.rv_add_recent);
    }

    public void GetRecentSong() {
        if (arrayList != null) {
            adapter = new AddBaiHatAdapter(getContext(), arrayList);
            if (arrayList.size() > 0) {
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
                recyclerView.setLayoutAnimation(animlayout);
                textView.setVisibility(View.INVISIBLE);
                return;
            }
        }

        textView.setVisibility(View.VISIBLE);
    }
}