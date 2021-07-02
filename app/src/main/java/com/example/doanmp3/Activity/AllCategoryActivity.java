package com.example.doanmp3.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AllCategoryAdapter;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        init();
        GetData();
    }

    private void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chủ đề & Thể loại");
        toolbar.setNavigationOnClickListener(v -> finish());
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
                adapterChude = new AllCategoryAdapter(AllCategoryActivity.this, ChuDeList, 1);
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
                adapterTheLoai = new AllCategoryAdapter(AllCategoryActivity.this, TheLoaiList, 2);

                rvTheLoai.setAdapter(adapterTheLoai);
                rvTheLoai.setLayoutManager(new GridLayoutManager(AllCategoryActivity.this, 2));
            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}