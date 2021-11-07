package com.example.doanmp3.Fragment.MainFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.doanmp3.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class NewsFragment extends Fragment {

    View view;
    TextInputEditText edtSearch;
    NavController navController;
    NavHostFragment navHostFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        InitControls();
        InitUI();
        handleEvent();
        return view;
    }

    private void InitControls() {
        edtSearch = view.findViewById(R.id.edt_search);
        navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.search_container);
        if (navHostFragment != null)
            navController = navHostFragment.getNavController();

    }

    private void InitUI() {
        edtSearch.requestFocus();
    }

    private void handleEvent() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyWord = Objects.requireNonNull(edtSearch.getText()).toString().trim();
                if (!keyWord.equals("")) {
                    NavigateToResult(keyWord);
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
                if (keyWord.equals("")) {
                    if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.searchResultFragment) {
                        navController.navigateUp();
                    }
                }
            }

        });
    }

    public void ClearKeyWord() {
        edtSearch.setText("");
    }

    private void NavigateToResult(String keyWord) {
        Bundle bundle = new Bundle();
        bundle.putString("keyWord", keyWord);
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.searchResultFragment) {
            navController.navigateUp();
        }
        navController.navigate(R.id.action_searchRecentFragment_to_searchResultFragment, bundle);
    }
}