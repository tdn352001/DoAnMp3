package com.example.doanmp3.Fragment.SearchFragment;

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

import com.example.doanmp3.Interface.KeyWordClick;
import com.example.doanmp3.NewModel.KeyWord;
import com.example.doanmp3.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SearchFragment extends Fragment implements KeyWordClick {

    View view;
    TextInputEditText edtSearch;
    NavController navController;
    NavHostFragment navHostFragment;
    FirebaseUser user;
    DatabaseReference searchRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        InitControls();
        InitUI();
        InitFirebaseData();
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

    private void InitFirebaseData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        searchRef = FirebaseDatabase.getInstance().getReference("search").child(user.getUid());
    }

    private void handleEvent() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyWord = Objects.requireNonNull(edtSearch.getText()).toString().trim();
                if (!keyWord.equals("")) {
                    NavigateToResult(keyWord);
                    SaveKeyWord(keyWord);
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

    private void SaveKeyWord(String keyWord){
        String key = searchRef.push().getKey();
        KeyWord newKeyWord = new KeyWord(key, keyWord);
        if (key != null) {
            searchRef.child(key).setValue(newKeyWord);
        }
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

    @Override
    public void onKeyWordClick(String keyWord) {
        edtSearch.setText(keyWord);
        edtSearch.clearFocus();
        NavigateToResult(keyWord);
    }
}