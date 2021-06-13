package com.example.doanmp3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.UserBaiHatPlaylistAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

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
    String IdPlaylist;
    String TenPlaylist;
    public static ArrayList<BaiHat> arrayList;
    public static UserBaiHatPlaylistAdapter adapter;
    public static User user;


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
        if (intent.hasExtra("idplaylist")) {
            IdPlaylist = intent.getStringExtra("idplaylist");
            TenPlaylist = intent.getStringExtra("tenplaylist");
        }
        btnAddBaiHat.setClickable(false);
    }

    private void GetBaiHatPlaylist() {
        arrayList = null;
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetUserBaiHatPlaylist(user.getIdUser(), IdPlaylist);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();

                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                setRV();
                Log.e("BBB", "Lay Xong Thong Tin");
                btnAddBaiHat.setClickable(true);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                btnAddBaiHat.setClickable(true);
            }
        });
    }

    private void EventClick() {
        btnAddBaiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserPlaylistActivity.this, AddBaiHatActivity.class);
                intent.putExtra("idplaylist", IdPlaylist);
                intent.putExtra("tenplaylist", TenPlaylist);
                intent.putExtra("baihat", arrayList);
                startActivity(intent);
            }
        });
        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList != null) {
                    if (arrayList.size() > 0) {
                        Intent intentt = new Intent(DetailUserPlaylistActivity.this, PlayNhacActivity.class);
                        Random rd = new Random();
                        DanhSachBaiHatActivity.category = "Playlist";
                        DanhSachBaiHatActivity.TenCategoty = TenPlaylist;
                        intentt.putExtra("mangbaihat", arrayList);
                        intentt.putExtra("position", rd.nextInt(arrayList.size()));
                        PlayNhacActivity.random = true;
                        startActivity(intentt);
                    }
                }
            }
        });
    }

    private void setRV(){
        adapter = new UserBaiHatPlaylistAdapter(DetailUserPlaylistActivity.this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailUserPlaylistActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}