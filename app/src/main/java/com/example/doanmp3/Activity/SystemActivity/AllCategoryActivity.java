package com.example.doanmp3.Activity.SystemActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.GenreAdapter;
import com.example.doanmp3.Interface.DataService;
import com.example.doanmp3.Models.Genre;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoryActivity extends BaseActivity {

    Toolbar toolbar;
    RecyclerView rvTheme, rvCategory;

    ArrayList<Genre> themes;
    ArrayList<Genre> categories;
    GenreAdapter themeAdapter, categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        InitControls();
        GetGenres();
        SetToolBar();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.tool_bar);
        rvTheme = findViewById(R.id.rv_theme);
        rvCategory = findViewById(R.id.rv_category);
    }

    private void GetGenres(){
        DataService dataService = APIService.getService();
        Call<List<List<Genre>>> callback = dataService.getAllGenreAndTheme();
        callback.enqueue(new Callback<List<List<Genre>>>() {
            @Override
            public void onResponse(@NonNull Call<List<List<Genre>>> call, @NonNull Response<List<List<Genre>>> response) {
                List<List<Genre>> lists = response.body();
                if(lists != null)
                {
                    themes = (ArrayList<Genre>) lists.get(0);
                    categories = (ArrayList<Genre>) lists.get(1);
                    SetUpRecyclerView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<List<Genre>>> call, @NonNull Throwable t) {

            }
        });
    }

    private void SetToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void SetUpRecyclerView() {
        themeAdapter = new GenreAdapter(this, themes, position -> {
            Intent intent = new Intent(AllCategoryActivity.this, DetailCategoryActivity.class);
            intent.putExtra("theme", themes.get(position));
            startActivity(intent);
        });
        rvTheme.setAdapter(themeAdapter);
        rvTheme.setLayoutManager(new GridLayoutManager(this, 2));

        categoryAdapter = new GenreAdapter(this, categories, position -> {
            Intent intent = new Intent(AllCategoryActivity.this, DetailCategoryActivity.class);
            intent.putExtra("category", categories.get(position));
            startActivity(intent);
        });
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setLayoutManager(new GridLayoutManager(this, 2));
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}