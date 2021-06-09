package com.example.doanmp3.Fragment.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.AllSingerActivity;
import com.example.doanmp3.Activity.MainActivity;
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


public class SingerFragment extends Fragment {

    View view;
    TextView txt;
    RecyclerView recyclerView;
    public SingerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singer, container, false);
        txt = view.findViewById(R.id.txt_singer);
        recyclerView = view.findViewById(R.id.rv_singer);
        GetData();


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSingerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<CaSi>> callback = dataService.GetRanDomCaSi();
        callback.enqueue(new Callback<List<CaSi>>() {
            @Override
            public void onResponse(Call<List<CaSi>> call, Response<List<CaSi>> response) {
                ArrayList<CaSi> caSis = (ArrayList<CaSi>) response.body();
                AllSingerAdapter adapter = new AllSingerAdapter(getActivity() ,caSis);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                MainActivity.LoadingComplete();


            }

            @Override
            public void onFailure(Call<List<CaSi>> call, Throwable t) {

            }
        });
    }
}