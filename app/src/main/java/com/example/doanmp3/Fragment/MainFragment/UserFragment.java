package com.example.doanmp3.Fragment.MainFragment;

import static android.app.Activity.RESULT_OK;
import static com.example.doanmp3.Service.Tools.SetTextStyle;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.doanmp3.NewActivity.ChangeInfoUserActivity;
import com.example.doanmp3.NewActivity.SettingsActivity;
import com.example.doanmp3.NewAdapter.ViewPager2StateAdapter;
import com.example.doanmp3.NewModel.User;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    View view;
    RoundedImageView userBanner;
    CircleImageView userAvatar;
    TextView tvUsername, tvUserDescription;
    MaterialButton btnEdit, btnSettings;
    CardView btnSong, btnAlbum, btnSinger, btnDevice;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPager2StateAdapter adapter;
    UserPlaylistFragment userPlaylistFragment;
    UserLoveSongFragment userLoveSongFragment;


    FirebaseAuth auth;
    FirebaseUser user;
    User dataUser;
    FirebaseDatabase database;
    DatabaseReference userReference;

    private final ActivityResultLauncher<Intent> changeInfoUser = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            user = auth.getCurrentUser();
            if(user != null) {
                Glide.with(requireContext()).load(user.getPhotoUrl()).into(userAvatar);
                tvUsername.setText(user.getDisplayName());
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user2, container, false);
        InitComponents();
        InitTabLayout();
        GetUserInfo();
        HandleEvents();
        return view;
    }

    private void InitComponents() {
        userBanner = view.findViewById(R.id.img_user_banner);
        userAvatar = view.findViewById(R.id.img_user_avatar);
        tvUsername = view.findViewById(R.id.txt_username);
        tvUserDescription = view.findViewById(R.id.user_description);
        btnEdit = view.findViewById(R.id.btn_edit_user_info);
        btnSettings = view.findViewById(R.id.btn_settings);
        btnSong = view.findViewById(R.id.btn_song);
        btnAlbum = view.findViewById(R.id.btn_album);
        btnSinger = view.findViewById(R.id.btn_singer);
        btnDevice = view.findViewById(R.id.btn_on_device);
        tabLayout = view.findViewById(R.id.tab_layout_user);
        viewPager = view.findViewById(R.id.viewpager_user);
    }

    private void InitTabLayout() {
        // Init fragments list
        ArrayList<Fragment> fragments = new ArrayList<>();
        userPlaylistFragment = new UserPlaylistFragment();
        userLoveSongFragment = new UserLoveSongFragment();
        fragments.add(userPlaylistFragment);
        fragments.add(userLoveSongFragment);

        // Init tilte list
        ArrayList<String> titleTab = new ArrayList<>();
        titleTab.add(getString(R.string.playlist));
        titleTab.add(getString(R.string.favourite));

        // init adapter
        adapter = new ViewPager2StateAdapter(this, fragments, titleTab);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (adapter.getTitles() != null && position < adapter.getTitles().size()) {
                tab.setText(adapter.getTitles().get(position));
            }
        }).attach();

        int SelectedTabPosition = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tabSelected = tabLayout.getTabAt(SelectedTabPosition);
        if (tabSelected != null) {
            String tabSelectedTitle = Objects.requireNonNull(tabSelected.getText()).toString();
            tabSelected.setText(SetTextStyle(tabSelectedTitle, Typeface.BOLD));
        }
    }

    private void GetUserInfo() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users").child(user.getUid());

        if(user != null) {
            Glide.with(requireContext())
                    .load(user.getPhotoUrl())
                    .error(R.drawable.person)
                    .into(userAvatar);
            tvUsername.setText(user.getDisplayName());
        }

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataUser = snapshot.getValue(User.class);
                if(dataUser != null){
                    tvUserDescription.setText(dataUser.getDescription());
                    Glide.with(requireContext())
                            .load(dataUser.getBannerUri())
                            .error(R.drawable.banner)
                            .into(userBanner);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void HandleEvents() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeInfoUserActivity.class);
            changeInfoUser.launch(intent);
        });
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

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
    }

}