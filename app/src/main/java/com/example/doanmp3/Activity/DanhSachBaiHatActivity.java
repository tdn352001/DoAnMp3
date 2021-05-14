package com.example.doanmp3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.doanmp3.Adapter.AllSongAdapter;
import com.example.doanmp3.Adapter.SongAdapter;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class DanhSachBaiHatActivity extends AppCompatActivity {

    // Mặc định
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton button;
    LinearLayoutManager linearLayoutManager;
    ImageView imageView;
    public static String category = "";
    public static  String TenCategoty= "";
    ProgressBar progressBar;
    // Tùy Biến
    Album album;
    Playlist playlist;
    ChuDeTheLoai cdtl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_bai_hat);
        AnhXa();
        GetIntent();
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void GetIntent()  {
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("album"))
            {
                album = (Album) intent.getSerializableExtra("album");
                category = "Album";
                TenCategoty = album.getTenAlbum();
                SetValueView(album.getTenAlbum(), album.getHinhAlbum());
                GetDataAlbum(album.getIdAlbum());
            }
            else
            {
                if(intent.hasExtra("playlist"))
                {
                    playlist = (Playlist) intent.getSerializableExtra("playlist");
                    category = "Playlist";
                    TenCategoty = playlist.getTen();
                    SetValueView(playlist.getTen(), playlist.getHinhAnh());
                    GetDataPlaylist(playlist.getIdPlaylist());
                }
                else
                {
                    if(intent.hasExtra("ChuDe")){
                        cdtl = (ChuDeTheLoai) intent.getSerializableExtra("ChuDe");
                        category="Chủ Đề";
                        TenCategoty = cdtl.getTen();
                        SetValueView(cdtl.getTen(), cdtl.getHinh());
                        GetDataChuDe(cdtl.getId());
                    }
                    else{
                        cdtl = (ChuDeTheLoai) intent.getSerializableExtra("TheLoai");
                        category="Thể Loại";
                        TenCategoty = cdtl.getTen();
                        SetValueView(cdtl.getTen(), cdtl.getHinh());
                        GetDataTheLoai(cdtl.getId());
                    }
                }
            }
        }
        
    }



    private void AnhXa(){
        imageView = findViewById(R.id.img_tieudedanhsach);
        coordinatorLayout = findViewById(R.id.coordinatorlayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbarlayout);
        toolbar = findViewById(R.id.toolbar_dsbh);
        recyclerView = findViewById(R.id.rv_dsbh);
        button = findViewById(R.id.btn_action);
        progressBar = findViewById(R.id.progress_load_baihat);
        linearLayoutManager = new LinearLayoutManager(DanhSachBaiHatActivity.this, LinearLayoutManager.VERTICAL, false);
    }


    private  void SetValueView(String Ten, String Hinh){
        collapsingToolbarLayout.setTitle(Ten);
        Picasso.with(DanhSachBaiHatActivity.this).load(Hinh).into(imageView);
    }

    private void GetDataAlbum(String id){

        DataService dataService= APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatAlbum(id);

        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> arrayList = (ArrayList<BaiHat>) response.body();
                AllSongAdapter adapter = new AllSongAdapter(DanhSachBaiHatActivity.this, arrayList);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Toast.makeText(DanhSachBaiHatActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void GetDataPlaylist(String id){
        DataService dataService= APIService.getService();
        Call<List<BaiHat>> call = dataService.GetBaiHatPlaylist(id);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> baiHats = (ArrayList<BaiHat>) response.body();
                AllSongAdapter adapter = new AllSongAdapter(DanhSachBaiHatActivity.this, baiHats);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Toast.makeText(DanhSachBaiHatActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDataChuDe(String id){
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatChuDe(id);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> baihatchude = (ArrayList<BaiHat>) response.body();
                AllSongAdapter adapter = new AllSongAdapter(DanhSachBaiHatActivity.this, baihatchude);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Toast.makeText(DanhSachBaiHatActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void GetDataTheLoai(String id){
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatTheLoai(id);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> baihatchude = (ArrayList<BaiHat>) response.body();
                AllSongAdapter adapter = new AllSongAdapter(DanhSachBaiHatActivity.this, baihatchude);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Toast.makeText(DanhSachBaiHatActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
            }
        });
    }



}