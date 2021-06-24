package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AllSingerAdapter;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchcasiFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RelativeLayout layoutNoinfo;
    private AllSingerAdapter adapter;
    public static ArrayList<CaSi> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_searchcasi, container, false);
        AnhXa();
        return view;
    }

    private void AnhXa() {

        recyclerView = view.findViewById(R.id.rv_search_casi);
        layoutNoinfo = view.findViewById(R.id.txt_search_casi_noinfo);
    }

    public void Search(String query) {
        recyclerView.removeAllViews();
        DataService dataService = APIService.getService();
        Call<List<CaSi>> callback = dataService.GetSearchCaSi(query);
        callback.enqueue(new Callback<List<CaSi>>() {
            @Override
            public void onResponse(Call<List<CaSi>> call, Response<List<CaSi>> response) {
                arrayList = (ArrayList<CaSi>) response.body();
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
            public void onFailure(Call<List<CaSi>> call, Throwable t) {

            }
        });
    }

    private void SetRecycleView() {
        adapter = new AllSingerAdapter(getContext(), arrayList, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
}
