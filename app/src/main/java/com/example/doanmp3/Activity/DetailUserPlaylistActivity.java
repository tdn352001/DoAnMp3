package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.UserBaiHatPlaylistAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserPlaylistActivity extends AppCompatActivity {

    RoundedImageView imgPlaylist;
    Toolbar toolbar;
    FloatingActionButton btnPlayAll;
    RelativeLayout btnAddBaiHat;
    RecyclerView recyclerView;
    TextView txtNoInf;
    ArrayList<BaiHat> arrayList;
    UserBaiHatPlaylistAdapter adapter;
    String IdPlaylist;
    String TenPlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_playlist);
        AnhXa();
        GetIntent();
        SetupToolBar();
        GetBaiHatPlaylist();
        EventClick();
    }


    private void AnhXa() {
        imgPlaylist = findViewById(R.id.img_tieudedanhsach_userplaylist);
        toolbar = findViewById(R.id.toolbar_dsbh_userplaylist);
        btnAddBaiHat = findViewById(R.id.relative_btn_add_baihat_userplaylist);
        btnPlayAll = findViewById(R.id.btn_action_userplaylist);
        recyclerView = findViewById(R.id.rv_dsbh_userplaylist);
        txtNoInf = findViewById(R.id.txt_no_info_user_playlist);
    }

    private void SetupToolBar() {
        toolbar.setTitle(TenPlaylist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("idplaylist"))
            IdPlaylist = intent.getStringExtra("idplaylist");
        TenPlaylist = intent.getStringExtra("tenplaylist");
    }

    private void GetBaiHatPlaylist() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetUserBaiHatPlaylist(MainActivity.user.getIdUser(), IdPlaylist);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();

                if (arrayList != null) {
                    if (arrayList.size() > 0) {
                        Picasso.with(DetailUserPlaylistActivity.this).load(arrayList.get(0).getHinhBaiHat().toString()).into(imgPlaylist);
                        adapter = new UserBaiHatPlaylistAdapter(DetailUserPlaylistActivity.this, arrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailUserPlaylistActivity.this);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void EventClick() {
        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList != null) {
                    if (arrayList.size() > 0) {
                        Intent intentt = new Intent(DetailUserPlaylistActivity.this, PlayNhacActivity.class);
                        Random rd = new Random();
                        intentt.putExtra("mangbaihat", arrayList);
                        intentt.putExtra("position", rd.nextInt(arrayList.size()));
                        PlayNhacActivity.random = true;
                        startActivity(intentt);
                    }
                }
            }
        });
    }


}