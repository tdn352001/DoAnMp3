package com.example.doanmp3.Fragment.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewActivity.SingerActivity;
import com.example.doanmp3.NewAdapter.ObjectCircleAdapter;
import com.example.doanmp3.NewModel.Object;
import com.example.doanmp3.NewModel.Singer;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SingerFragment extends Fragment {


    View view;
    RecyclerView recyclerView;

    ArrayList<Singer> singers;
    ArrayList<Object> objects;
    ObjectCircleAdapter objectAdapter;
    int connectAgainst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singer2, container, false);
        InitControls();
        GetDataSinger();
        return view;
    }

    private void InitControls() {
        recyclerView = view.findViewById(R.id.rv_singer);
        connectAgainst = 0;
    }

    private void GetDataSinger() {
        NewDataService dataService = APIService.newService();
        Call<List<Singer>> callback = dataService.getRandomSingers();
        callback.enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Singer>> call, @NonNull Response<List<Singer>> response) {
                singers = (ArrayList<Singer>) response.body();
                objects = new ArrayList<>();
                if (singers != null) {
                    for (Singer singer : singers){
                        objects.add(singer.convertToObject());
                    }
                }
                SetUpRecycleView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Singer>> call, @NonNull Throwable t) {
                if(connectAgainst < 3){
                    GetDataSinger();
                    connectAgainst++;
                    Log.e("ERROR","GetDataSinger:" + t.getMessage());
                }
            }
        });
    }

    private void SetUpRecycleView() {
        objectAdapter = new ObjectCircleAdapter(requireContext(), objects, position -> {
            Intent intent = new Intent(getContext(), SingerActivity.class);
            intent.putExtra("singer", (Parcelable) singers.get(position));
            startActivity(intent);
        });
        recyclerView.setAdapter(objectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }
}