package com.example.doanmp3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.doanmp3.Adapter.AllCategoryAdapter;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvChuDe, rvTheLoai;
    AllCategoryAdapter adapterChude, adapterTheLoai;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        AnhXa();
        init();
        GetData();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chủ đề & Thể loại");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_allcategory);
        rvChuDe = findViewById(R.id.rv_allchude);
        rvTheLoai = findViewById(R.id.rv_alltheloai);
        gridLayoutManager = new GridLayoutManager(AllCategoryActivity.this, 2);
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callChuDe = dataService.GetAllChuDe();
        Call<List<ChuDeTheLoai>> callTheLoai = dataService.GetAllTheLoai();

        callChuDe.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                ArrayList<ChuDeTheLoai> ChuDeList = (ArrayList<ChuDeTheLoai>) response.body();
                adapterChude = new AllCategoryAdapter(AllCategoryActivity.this, ChuDeList);
                rvChuDe.setLayoutManager(gridLayoutManager);
                rvChuDe.setAdapter(adapterChude);
            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });


        callTheLoai.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                ArrayList<ChuDeTheLoai> TheLoaiList = (ArrayList<ChuDeTheLoai>) response.body();
                adapterTheLoai = new AllCategoryAdapter(AllCategoryActivity.this, TheLoaiList);
                rvTheLoai.setAdapter(adapterTheLoai);
                rvTheLoai.setLayoutManager(new GridLayoutManager(AllCategoryActivity.this, 2));
            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });

    }
}