package com.example.doanmp3.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.doanmp3.Model.KeyWord;
import com.example.doanmp3.R;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;

public class RecentSearchFragment extends Fragment  {

    View view;

    // Phần Tìm Kiếm Gần Đây
    FlowLayout flowLayout;
    RelativeLayout SearchRecentLayout;
    TextView btnDelete;
    ArrayList<KeyWord> keyWordArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_search, container, false);
//        AnhXa();
//        GetResentKeyWord();
//        SetFlowLayout();

        return view;
    }



//    private void AnhXa() {
//        // Phần Tìm Kiếm Gần Đây
//        SearchRecentLayout = view.findViewById(R.id.layout_recent_search);
//        flowLayout = view.findViewById(R.id.flowlayout_history_search);
//        btnDelete = view.findViewById(R.id.btn_delete_history_search);
//    }
//    private void GetResentKeyWord() {
//        DataService dataService = APIService.getUserService();
//        Call<List<KeyWord>> callback = dataService.GetKeyWordRecent();
//        callback.enqueue(new Callback<List<KeyWord>>() {
//            @Override
//            public void onResponse(Call<List<KeyWord>> call, Response<List<KeyWord>> response) {
//                keyWordArrayList = (ArrayList<KeyWord>) response.body();
//            }
//
//            @Override
//            public void onFailure(Call<List<KeyWord>> call, Throwable t) {
//            }
//        });
//    }
//
//    private void SetFlowLayout() {
//        if (flowLayout != null)
//            return;
//
//        flowLayout.removeAllViews();
//
//        if (keyWordArrayList != null) {
//            for (int i = 0; i < keyWordArrayList.size(); i++) {
//                TextView textView = new TextView(getContext());
//                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(10, 10, 0, 0);
//                textView.setLayoutParams(params);
//                textView.setId(Integer.parseInt(keyWordArrayList.get(i).getIdSearch()));
//                textView.setText(keyWordArrayList.get(i).getKeyWord());
//                textView.setPadding(5,2,5,2);
//                textView.setBackgroundResource(R.drawable.custom_item_flowlayout);
//                textView.setTextColor(getResources().getColor(R.color.purple_500));
//                textView.setOnClickListener(this);
//                flowLayout.addView(textView);
//            }
//        }
//        else
//            SearchRecentLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
}