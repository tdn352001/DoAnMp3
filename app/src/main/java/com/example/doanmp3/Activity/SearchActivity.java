package com.example.doanmp3.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.doanmp3.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    TextInputEditText edtSearch;
    NavController navController;
    NavHostFragment navHostFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        handleEvent();
    }

    private void initUI() {
        edtSearch = findViewById(R.id.edt_search);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.search_container);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
    }

    private void handleEvent() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyWord = Objects.requireNonNull(edtSearch.getText()).toString().trim();
                if (!keyWord.equals("")) {
                    DisplayResult(keyWord);
                }
            }
            return false;
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWord = s.toString();
                if(keyWord.equals("")){
                    if(Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.searchResultFragment){
                        navController.navigateUp();
                    }
                }
            }

        });
    }

    private void DisplayResult(String keyWord) {
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyWord);
        navController.navigate(R.id.action_searchRecentFragment_to_searchResultFragment, bundle);
    }
}

