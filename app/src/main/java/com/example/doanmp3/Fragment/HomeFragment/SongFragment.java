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

import com.example.doanmp3.Activity.AllSongActivity;
import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SongFragment extends Fragment {
    View view;
    TextView txt;
    RecyclerView recyclerView;
    SongAdapter adapter;

    public SongFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        txt = view.findViewById(R.id.txt_song);
        recyclerView = view.findViewById(R.id.rv_song);
        GetData();


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSongActivity.class);
                intent.putExtra("mangbaihat", adapter.getallarraylist());
                startActivity(intent);
            }
        });
        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllSong();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> songs = (ArrayList<BaiHat>) response.body();
                adapter = new SongAdapter(getActivity(), songs);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                MainActivity.LoadingComplete();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }
}