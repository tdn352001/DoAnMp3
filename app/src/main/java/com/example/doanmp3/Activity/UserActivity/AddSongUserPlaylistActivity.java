package com.example.doanmp3.Activity.UserActivity;

import static com.example.doanmp3.Service.Tools.SetTextStyle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmp3.Activity.SystemActivity.BaseActivity;
import com.example.doanmp3.Adapter.ViewPager2StateAdapter;
import com.example.doanmp3.Context.Constant.FirebaseRef;
import com.example.doanmp3.Fragment.UserPlaylist.AddedSongFragment;
import com.example.doanmp3.Fragment.UserPlaylist.DeviceSongFragment;
import com.example.doanmp3.Fragment.UserPlaylist.LoveSongFragment;
import com.example.doanmp3.Fragment.UserPlaylist.OnlineSongFragment;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.Models.UserPlaylist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class AddSongUserPlaylistActivity extends BaseActivity {

    // Control
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    TextInputEditText edtSearch;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ProgressBar progressBar;
    TextView btnFinish;

    // Playlist
    UserPlaylist playlist;
    ArrayList<Song> songs;
    ArrayList<Song> oldSongs;

    // Fragment
    AddedSongFragment addedSongFragment;
    OnlineSongFragment onlineSongFragment;
    LoveSongFragment loveSongFragment;
    DeviceSongFragment deviceSongFragment;

    // ViewPagerData
    ViewPager2StateAdapter adapter;
    ArrayList<Fragment> fragments;
    ArrayList<String> titleTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_user_playlist);
        InitControls();
        InitDataViewPager();
        SetUpViewPagerAndTab();
        GetIntent();
        HandleEvents();
    }

    private void InitControls() {
        coordinatorLayout = findViewById(R.id.layout_container);
        toolbar = findViewById(R.id.tool_bar_add_song);
        btnFinish = findViewById(R.id.btn_finish);
        edtSearch = findViewById(R.id.edt_search);
        tabLayout = findViewById(R.id.tab_layout_result);
        viewPager = findViewById(R.id.viewpager_result);
        progressBar = findViewById(R.id.progress_bar_load_result);
    }

    private void InitDataViewPager() {
        fragments = new ArrayList<>();
        addedSongFragment = new AddedSongFragment();
        onlineSongFragment = new OnlineSongFragment();
        loveSongFragment = new LoveSongFragment();
        deviceSongFragment = new DeviceSongFragment();
        fragments.add(addedSongFragment);
        fragments.add(onlineSongFragment);
        fragments.add(loveSongFragment);
        fragments.add(deviceSongFragment);

        titleTab = new ArrayList<>();
        titleTab.add(getString(R.string.added));
        titleTab.add(getString(R.string.online));
        titleTab.add(getString(R.string.favourite));
        titleTab.add(getString(R.string.on_device));
    }

    private void SetUpViewPagerAndTab() {
        // Setup ViewPager
        adapter = new ViewPager2StateAdapter(this, fragments, titleTab);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        // Set up ViewPager With TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (adapter.getTitles() != null && position < adapter.getTitles().size()) {
                tab.setText(adapter.getTitles().get(position));
            }
        }).attach();

        // Set Text Style For Selected Tab
        int SelectedTabPosition = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tabSelected = tabLayout.getTabAt(SelectedTabPosition);
        if (tabSelected != null) {
            String tabSelectedTitle = Objects.requireNonNull(tabSelected.getText()).toString();
            tabSelected.setText(SetTextStyle(tabSelectedTitle, Typeface.BOLD));
        }
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("playlist")) {
            playlist = intent.getParcelableExtra("playlist");
            songs = playlist.getSongs();
            if (songs == null) songs = new ArrayList<>();

            oldSongs = new ArrayList<>(songs);

            addedSongFragment.SetUpRecyclerViewSafety(songs);
            onlineSongFragment.setAddedSong(songs);
            loveSongFragment.setAddedSong(songs);
            SetToolBar();
        }
    }

    private void SetToolBar() {
        toolbar.setTitle(playlist.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> ExistActivity());
    }

    private void HandleEvents() {
        coordinatorLayout.setOnClickListener(v -> Tools.hideSoftKeyBoard(AddSongUserPlaylistActivity.this));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabTitle = Objects.requireNonNull(tab.getText()).toString();
                SpannableStringBuilder tabBoldTitle = SetTextStyle(tabTitle, Typeface.BOLD);
                tab.setText(tabBoldTitle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                String tabTitle = Objects.requireNonNull(tab.getText()).toString();
                SpannableStringBuilder tabBoldTitle = SetTextStyle(tabTitle, Typeface.NORMAL);
                tab.setText(tabBoldTitle);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

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
                String query = edtSearch.getText().toString();
                addedSongFragment.QueryData(query);
                onlineSongFragment.QueryData(query);
                loveSongFragment.QueryData(query);
                deviceSongFragment.QueryData(query);
            }
        });

        btnFinish.setOnClickListener(v -> {
            if (isNotChange()) {
                finish();
            } else {
                SaveAddedSong();
            }
        });
    }

    private void SaveAddedSong() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userPlaylist = FirebaseDatabase.getInstance()
                .getReference(FirebaseRef.USER_PLAYLISTS).child(user.getUid()).child(playlist.getId())
                .child(FirebaseRef.SONGS);

        userPlaylist.setValue(songs).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("songs", songs);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private boolean isNotChange() {
        return oldSongs.equals(songs);
    }

    private void ExistActivity() {
        if (isNotChange())
            finish();
        else
            ShowExistDialog();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void ShowExistDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setTitle(R.string.exit);
        dialog.setBackground(getResources().getDrawable(R.drawable.rounded_background));
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setMessage(R.string.save_result);
        dialog.setNegativeButton(R.string.yes, (dialog12, which) -> SaveAddedSong());
        dialog.setPositiveButton(R.string.cancel, (dialog1, which) -> finish());
        dialog.show();
    }

    public void UpdateAddedSong(boolean isAdd, Song song) {
        if (isAdd)
            InsertAddedSong(song);
        else
            RemoveAddedSong(song);

        String query = edtSearch.getText().toString();
        if (!query.equals("")) {
            addedSongFragment.QueryData(query);
            onlineSongFragment.NotifyDataChange();
            loveSongFragment.NotifyDataChange();
            deviceSongFragment.NotifyDataChange();
        }
    }

    public void InsertAddedSong(Song song) {
        addedSongFragment.InsertAddedSong(song);
    }

    public void RemoveAddedSong(Song song) {
        addedSongFragment.RemoveAddedSong(song);
    }

    public void UpdateAddedSongLoveFragment(boolean isChecked, String idSong) {
        loveSongFragment.AddOrRemoveAddedSong(isChecked, idSong);
    }

    public void UpdateAddedSongOnlineFragment(boolean isChecked, String idSong) {
        onlineSongFragment.AddOrRemoveAddedSong(isChecked, idSong);
    }

    public void UpdateAddedSongDeviceFragment(boolean isChecked, String uri){
        deviceSongFragment.AddOrRemoveAddedSong(isChecked, uri);
    }
    @Override
    public void onBackPressed() {
        ExistActivity();
    }
}