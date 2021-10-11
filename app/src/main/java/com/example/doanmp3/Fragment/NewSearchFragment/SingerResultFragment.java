package com.example.doanmp3.Fragment.NewSearchFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.SingerAdapter;
import com.example.doanmp3.NewModel.Singer;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.List;

public class SingerResultFragment extends Fragment {

    View view;
    RecyclerView rvSinger;
    ArrayList<Singer> singers;
    SingerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singer_result, container, false);
        InitControls();
        InitData();
        SetUpRecycleView();
        return view;
    }
    private void InitControls() {
        rvSinger = view.findViewById(R.id.rv_search_result_singer);
    }

    private void InitData() {
        singers = new ArrayList<>();
        adapter = new SingerAdapter(getActivity(), singers, new SingerAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        }, (itemView, position) -> {
            LinearLayout itemSinger = itemView.findViewById(R.id.layout_item_singer);
            int paddingSize = (int) getResources().getDimensionPixelSize(R.dimen._12dp);
            itemSinger.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            if(position % 2 == 0){
                itemView.setBackgroundResource(R.color.alabaster);
            }else{
                itemView.setBackgroundResource(R.color.white);
            }

        });
    }

    private void SetUpRecycleView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSinger.setAdapter(adapter);
        rvSinger.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvSinger.setLayoutAnimation(layoutAnimation);
    }

    public void DisplayResult(List<Singer> singersResult){
        if(singersResult == null){
            Log.e("EEE", "Result Null");
            return;
        }
        singers.clear();
        singers = (ArrayList<Singer>) singersResult;
        adapter.setSingers(singers);
    }

}