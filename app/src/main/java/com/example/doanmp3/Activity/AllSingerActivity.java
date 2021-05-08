package com.example.doanmp3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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

public class AllSingerActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AllSingerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_singer);
        AnhXa();
        init();
        GetData();
    }
    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<CaSi>> callback = dataService.GetAllSinger();
        callback.enqueue(new Callback<List<CaSi>>() {
            @Override
            public void onResponse(Call<List<CaSi>> call, Response<List<CaSi>> response) {
                ArrayList<CaSi> arrayList = (ArrayList<CaSi>) response.body();
                adapter = new AllSingerAdapter(AllSingerActivity.this, arrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(AllSingerActivity.this, 2));
            }

            @Override
            public void onFailure(Call<List<CaSi>> call, Throwable t) {

            }
        });

    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nghệ Sĩ");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_allsinger);
        recyclerView = findViewById(R.id.rv_allsinger);
    }
}