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

import com.example.doanmp3.Adapter.AddBaiHatAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Online_AddFragment extends Fragment {

    View view;
    RelativeLayout textView;
    RecyclerView recyclerView;
    public AddBaiHatAdapter adapter;
    public ArrayList<BaiHat> arrayList;
    public ArrayList<BaiHat> SearchArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online__add, container, false);
        AnhXa();
        GetTopBaiHat();
        return view;
    }


    private void AnhXa() {
        textView = view.findViewById(R.id.txt_noinfo_add_online);
        recyclerView = view.findViewById(R.id.rv_add_online);
    }

    public void GetTopBaiHat() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllSong();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                SearchArrayList = arrayList;
                SetRv();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    public void SetRv() {
        adapter = new AddBaiHatAdapter(getContext(), SearchArrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void SetResultBaiHat() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (recyclerView != null) {
                    recyclerView.removeAllViews();
                    if (SearchArrayList != null) {
                        SetRv();
                        if (SearchArrayList.size() > 0) {
                            textView.setVisibility(View.GONE);
                            handler.removeCallbacks(this);
                            return;
                        }

                    }
                    textView.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(this);
                }
            }
        };
        handler.postDelayed(runnable, 100);

    }
}