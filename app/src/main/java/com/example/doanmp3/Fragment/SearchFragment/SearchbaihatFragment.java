package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.SearchSongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchbaihatFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    RelativeLayout layoutNoinfo;
    SearchSongAdapter adapter;
    public ArrayList<BaiHat> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_searchbaihat, container, false);

        AnhXa();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_search_baihat);
        layoutNoinfo = view.findViewById(R.id.txt_search_baihat_noinfo);
        arrayList = new ArrayList<>();
    }

    public void Search(String query) {
        recyclerView.removeAllViews();
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetSearchBaiHat(query);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();

                SearchFragment.CountResult();
                if (arrayList != null) {
                    if (arrayList.size() > 0) {
                        layoutNoinfo.setVisibility(View.INVISIBLE);
                        SetRecycleView();
                        return;
                    }
                }
                layoutNoinfo.setVisibility(View.VISIBLE);
                SetRecycleView();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void SetRecycleView() {
        adapter = new SearchSongAdapter(getContext(), arrayList, true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}
