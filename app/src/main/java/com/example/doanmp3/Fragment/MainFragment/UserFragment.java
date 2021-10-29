package com.example.doanmp3.Fragment.MainFragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.doanmp3.NewActivity.ChangeInfoUserActivity;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    View view;
    RoundedImageView userBanner;
    CircleImageView userAvatar;
    TextView tvUsername, tvUserDescription;
    MaterialButton btnEdit;
    CardView btnSong, btnAlbum, btnSinger, btnDevice;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference userReference;

    private final ActivityResultLauncher<Intent> changeInfoUser = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.e("EEE", "GET RESULT");
        if (result.getResultCode() == RESULT_OK) {
            Log.e("EEE", "GET RESULT_OK");
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
        btnSong = view.findViewById(R.id.btn_song);
        btnAlbum = view.findViewById(R.id.btn_album);
        btnSinger = view.findViewById(R.id.btn_singer);
        btnDevice = view.findViewById(R.id.btn_on_device);
        tabLayout = view.findViewById(R.id.tab_layout_user);
        viewPager = view.findViewById(R.id.viewpager_user);
    }

    private void GetUserInfo() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users");

        if(user != null) {
            Glide.with(requireContext()).load(user.getPhotoUrl()).into(userAvatar);
            tvUsername.setText(user.getDisplayName());
        }
    }

    private void HandleEvents() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeInfoUserActivity.class);
            changeInfoUser.launch(intent);
        });
    }

}