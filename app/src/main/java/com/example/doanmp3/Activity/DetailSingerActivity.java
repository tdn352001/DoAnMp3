package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Adapter.AlbumAdapter;
import com.example.doanmp3.Adapter.AllSongAdapter;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSingerActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    RecyclerView recyclerViewBaiHat, recyclerViewAlBum;
    MaterialButton btn;
    ProgressBar progressBar;
    TextView txtAlbum;
    ArrayList<BaiHat> baiHatArrayList;
    ArrayList<BaiHat> tempbaiHatArrayList;
    ArrayList<Album> albumArrayList;
    AllSongAdapter Songadapter;
    CaSi caSi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_singer);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        AnhXa();
        GetIntent();
        eventClick();
        setupToolBar();
    }



    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_detailsinger);
        progressBar = findViewById(R.id.progress_load_detailsinger);
        imageView = findViewById(R.id.img_detailsinger);
        btn = findViewById(R.id.btn_viewmore_baihat_detailsinger);
        recyclerViewAlBum = findViewById(R.id.rv_detailsinger_album);
        recyclerViewBaiHat = findViewById(R.id.rv_detailsinger_baihat);
        txtAlbum = findViewById(R.id.txt_detailsinger_album);
        tempbaiHatArrayList = new ArrayList<>();
    }

    private void GetIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra("CaSi")) {
            caSi = (CaSi) intent.getSerializableExtra("CaSi");
            btn.setClickable(false);
            getBaiHatCaSi(caSi.getIdCaSi());
            getAlbumCaSi(caSi.getIdCaSi());
            setInfoCaSi();
        }
    }



    private void getBaiHatCaSi(String IdCaSi) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatCaSi(IdCaSi);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                baiHatArrayList = (ArrayList<BaiHat>) response.body();
                if(baiHatArrayList == null)
                    return;
                btn.setClickable(true);
                ViewMore();
                setDataBaiHat();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void ViewMore(){
        if(baiHatArrayList.size() < 5){
            btn.setVisibility(View.INVISIBLE);
        }
        else
        {
            tempbaiHatArrayList.add(baiHatArrayList.get(0));
            tempbaiHatArrayList.add(baiHatArrayList.get(1));
            tempbaiHatArrayList.add(baiHatArrayList.get(2));
            tempbaiHatArrayList.add(baiHatArrayList.get(3));
        }
    }

    private void setDataBaiHat() {

        if( baiHatArrayList.size() < 5)
            Songadapter = new AllSongAdapter(this, baiHatArrayList);
        else
            Songadapter = new AllSongAdapter(this, tempbaiHatArrayList);
        recyclerViewBaiHat.setAdapter(Songadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailSingerActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        recyclerViewBaiHat.setLayoutAnimation(animlayout);
        recyclerViewBaiHat.setLayoutManager(linearLayoutManager);
    }

    private void getAlbumCaSi(String IdCaSi) {
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.GetAlbumCaSi(IdCaSi);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albumArrayList = (ArrayList<Album>) response.body();
                if(albumArrayList == null)
                    return;
                setDataAlbum();
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Toast.makeText(DetailSingerActivity.this, "Lấy Danh Sách Album Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setDataAlbum() {
        if (albumArrayList.size() > 0) {

            AlbumAdapter adapter = new AlbumAdapter(this, albumArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailSingerActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewAlBum.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerViewAlBum.setAdapter(adapter);
        }
        else
        {
            txtAlbum.setVisibility(View.INVISIBLE);
        }


    }

    private void setInfoCaSi() {
        Glide.with(this).load(caSi.getHinhCaSi()).error(R.drawable.karaoke_mic).into(imageView);
        toolbar.setTitle(caSi.getTenCaSi());
    }

    private void eventClick() {

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(Songadapter.getItemCount() < 5){
                    Songadapter.setArrayList(baiHatArrayList);
                    btn.setText("Hiển thị ít");
                }
                else{
                    btn.setText("Xem Tất Cả");
                    Songadapter.setArrayList(tempbaiHatArrayList);
                }
            }
        });
    }
    private void setupToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }

}