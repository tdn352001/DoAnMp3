package com.example.doanmp3.Activity.UserActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AlbumAdapter;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Album;
import com.example.doanmp3.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserFavoriteAlbumActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    NestedScrollView layoutContent;
    MaterialButton btnPlay;
    RecyclerView recyclerView;
    TextView tvNoData;

    FirebaseUser user;
    ArrayList<Album> albums;
    AlbumAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);
    }

    private void InitControl() {
        toolbar = findViewById(R.id.toolbar);
        layoutContent = findViewById(R.id.layout_content);
        btnPlay = findViewById(R.id.btn_action_play_random);
        recyclerView = findViewById(R.id.rv_song);
        tvNoData = findViewById(R.id.tv_no_data);

        btnPlay.setVisibility(View.GONE);
    }

    private void SetToolBar() {
        toolbar.setTitle(R.string.favourite_album);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void SetRecyclerView() {
        adapter = new AlbumAdapter(this, albums, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onOptionClick(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        recyclerView.setLayoutAnimation(layoutAnimation);
    }
}