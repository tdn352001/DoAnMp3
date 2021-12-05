package com.example.doanmp3.Fragment.SearchFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.SystemActivity.DetailCategoryActivity;
import com.example.doanmp3.Activity.SystemActivity.MainActivity;
import com.example.doanmp3.Adapter.GenreAdapter;
import com.example.doanmp3.Context.Constant.FirebaseRef;
import com.example.doanmp3.Interface.DataService;
import com.example.doanmp3.Interface.KeyWordClick;
import com.example.doanmp3.Models.Genre;
import com.example.doanmp3.Models.KeyWord;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchRecentFragment extends Fragment {

    /* =====Controls====*/
    View view;
    TextView btnDeleteHistory;
    LinearLayout layoutSearchRecent, layoutContainer;
    FlowLayout layoutKeyWord;
    RecyclerView rvCategory;


    /*==== Data ====*/
    static ArrayList<Genre> genres;
    GenreAdapter genreAdapter;
    FirebaseUser user;
    DatabaseReference searchRef;
    ChildEventListener childEventListener;
    ArrayList<KeyWord> keyWords;
    static boolean haveHistorySearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_recent, container, false);
        InitControls();
        GetData();
        InitFirebaseData();
        CheckHaveHistorySearch();
        onAddKeyWordRecent();
        HandleEvent();
        return view;
    }

    private void InitControls() {
        btnDeleteHistory = view.findViewById(R.id.btn_delete_history_search);
        layoutContainer = view.findViewById(R.id.layout_recent_search_container);
        layoutSearchRecent = view.findViewById(R.id.layout_recent_search);
        layoutKeyWord = view.findViewById(R.id.layout_key_word_recent);
        rvCategory = view.findViewById(R.id.rv_category_recent_fragment);
    }

    private void InitFirebaseData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        searchRef = FirebaseDatabase.getInstance().getReference(FirebaseRef.SEARCH).child(user.getUid());
        keyWords = new ArrayList<>();
    }

    private void CheckHaveHistorySearch() {
        if (haveHistorySearch) {
            layoutSearchRecent.setVisibility(View.VISIBLE);
        }
    }

    private void GetData() {
        if (genres != null) {
            InitRecyclerView();
            return;
        }
        DataService dataService = APIService.getService();
        Call<List<Genre>> callback = dataService.get4GenreAndTheme();
        callback.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(@NonNull Call<List<Genre>> call, @NonNull Response<List<Genre>> response) {
                genres = (ArrayList<Genre>) response.body();
                InitRecyclerView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Genre>> call, @NonNull Throwable t) {
                Log.e("ERROR", "getAllGenreAndTheme Failed: " + t.getMessage());
            }
        });
    }

    private void InitRecyclerView() {
        genreAdapter = new GenreAdapter(getContext(), genres,
                position -> {
                    Intent intent = new Intent(getContext(), DetailCategoryActivity.class);
                    String type = position < 4 ? "theme" : "category";
                    intent.putExtra(type, genres.get(position));
                    startActivity(intent);
                });
        rvCategory.setAdapter(genreAdapter);
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void HandleEvent() {
        btnDeleteHistory.setOnClickListener(v -> DeleteHistoryDialog());
        layoutContainer.setOnClickListener(v -> Tools.hideSoftKeyBoard(getActivity()));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void DeleteHistoryDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireContext());
        dialog.setTitle(R.string.delete_history_search_title);
        dialog.setMessage(R.string.are_you_sure);
        dialog.setIcon(R.drawable.error);
        dialog.setBackground(getResources().getDrawable(R.drawable.rounded_background));
        dialog.setPositiveButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        dialog.setNegativeButton(R.string.done, (dialog12, which) -> DeleteHistory());
        dialog.show();
    }

    private void DeleteHistory() {
        haveHistorySearch = false;
        layoutSearchRecent.setVisibility(View.GONE);
        keyWords.clear();
        layoutKeyWord.removeAllViews();
        searchRef.removeValue();
        Toast.makeText(getContext(), R.string.delete_successfully, Toast.LENGTH_SHORT).show();
    }

    private void onAddKeyWordRecent() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KeyWord keyWord = snapshot.getValue(KeyWord.class);
                if (keyWord != null)
                    AddKeyWord(keyWord);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        searchRef.addChildEventListener(childEventListener);

    }

    private boolean CheckKeyWordExits(KeyWord keyWord) {
        for (int i = 0; i < keyWords.size(); i++) {
            if (keyWord.getKeyWord().equals(keyWords.get(i).getKeyWord())) {
                searchRef.child(keyWords.get(i).getId()).removeValue();
                keyWords.remove(i);
                return true;
            }
        }
        return false;
    }

    private void CheckKeyWordsOverSize() {
        if (keyWords.size() > 8) {
            int lastIndex = keyWords.size() - 1;
            searchRef.child(keyWords.get(lastIndex).getId()).removeValue();
            keyWords.remove(lastIndex);
        }
    }

    private void AddKeyWord(KeyWord keyWord) {
        if (!CheckKeyWordExits(keyWord))
            CheckKeyWordsOverSize();

        if (!haveHistorySearch) {
            layoutSearchRecent.setVisibility(View.VISIBLE);
            haveHistorySearch = true;
        }

        keyWords.add(0, keyWord);
        layoutKeyWord.removeAllViews();
        for (int i = 0; i < keyWords.size(); i++) {
            AddKeyWordToLayout(i, keyWords.get(i));
        }
    }

    private void AddKeyWordToLayout(int index, KeyWord keyWord) {
        int _4dp = Tools.ConvertDpToPx(4, requireContext());
        TextView textView = new TextView(getContext());
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, _4dp * 3, _4dp * 2, 0);
        textView.setLayoutParams(params);
        textView.setId(index);
        textView.setText(keyWord.getKeyWord());
        textView.setPadding(_4dp * 2, _4dp, _4dp * 2, _4dp);
        textView.setTextSize(14);
        textView.setMaxLines(1);
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.custom_item_search_recent);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setOnClickListener(v -> HandleKeyWordClick(textView.getId()));
        layoutKeyWord.addView(textView);
    }

    private void HandleKeyWordClick(int index) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            KeyWordClick keyWordClick = mainActivity.searchFragment;
            keyWordClick.onKeyWordClick(keyWords.get(index).getKeyWord());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


