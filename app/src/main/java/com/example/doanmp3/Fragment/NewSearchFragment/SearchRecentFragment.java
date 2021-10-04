package com.example.doanmp3.Fragment.NewSearchFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.GenreAdapter;
import com.example.doanmp3.NewModel.Genre;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.NewDataService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchRecentFragment extends Fragment {

    /* =====Controls====*/
    View view;
    TextView btnDeleteHistory;
    LinearLayout layoutSearchRecent;
    RecyclerView rvCategory;



   /*==== Data ====*/
    ArrayList<Genre> genres;
    GenreAdapter genreAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_recent, container, false);
        InitControls();
        GetData();
        HandleEvent();
        return view;
    }

    private void InitControls() {
        btnDeleteHistory = view.findViewById(R.id.btn_delete_history_search);
        layoutSearchRecent = view.findViewById(R.id.layout_recent_search);
        rvCategory = view.findViewById(R.id.rv_category_recent_fragment);
    }

    private void GetData() {
        NewDataService dataService = APIService.newService();
        Call<List<Genre>> callback = dataService.getAllGenreAndTheme();
        callback.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(@NonNull Call<List<Genre>> call, @NonNull Response<List<Genre>> response) {
                genres = (ArrayList<Genre>) response.body();
                InitRecyclerView();
            }
            @Override
            public void onFailure(@NonNull Call<List<Genre>> call, @NonNull Throwable t) {
                Log.e("EEE", "getAllGenreAndTheme Failed: " + t.getMessage());
            }
        });
    }

    private void InitRecyclerView() {
        genreAdapter = new GenreAdapter(getContext(), genres,
                position -> Log.e("EEE", position + ""));
        rvCategory.setAdapter(genreAdapter);
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void HandleEvent() {
        btnDeleteHistory.setOnClickListener(v -> DeleteHistoryDialog());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void DeleteHistoryDialog(){
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireContext());
        dialog.setTitle(R.string.delete_history_search_title);
        dialog.setMessage(R.string.are_you_sure);
        dialog.setIcon(R.drawable.error);
        dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
        dialog.setPositiveButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        dialog.setNegativeButton(R.string.done, (dialog12, which) -> DeleteHistory());
        dialog.show();
    }

    private void DeleteHistory() {
//        DataService dataService = APIService.getUserService();
//        Call<String> callback =dataService.DeleteSearch(MainActivity.user.getIdUser());
//        callback.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String rel = (String) response.body();
//                if (rel != null)
//                    if (rel.equals("S")) {
//
//                    }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
    }
}