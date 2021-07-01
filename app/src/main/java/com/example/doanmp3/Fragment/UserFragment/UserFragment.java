package com.example.doanmp3.Fragment.UserFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Activity.LoginActivity;
import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Activity.UserInfoActivity;
import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.LoginFragment.LoginFragment;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    View view;
    MaterialButton btnEdit, btnLogout;
    public UserPlaylistFragment userPlaylistFragment;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtUserName;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imgBanner;
    public static CircleImageView imgAvatar;
    TabLayout tabLayout;
    ViewPager viewPager;
    public static ViewPagerAdapter adapter;
    public static final int PERMISSION_READ = 0;
    public static final int PERMISSION_WRITE = 1;
    @SuppressLint("StaticFieldLeak")
    public static UserBaiHatFragment userBaiHatFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        AnhXa();
        SetupInfoUser();
        SetupViewPager();
        checkPermission();
        EventClick();
        return view;
    }


    private void AnhXa() {
        btnEdit = view.findViewById(R.id.btn_edit_user_info);
        btnLogout = view.findViewById(R.id.btn_logout);
        imgAvatar = view.findViewById(R.id.img_user_avatar);
        imgBanner = view.findViewById(R.id.img_user_banner);
        txtUserName = view.findViewById(R.id.txt_username);
        tabLayout = view.findViewById(R.id.tablayout_user);
        viewPager = view.findViewById(R.id.viewpager_user);
    }

    public void SetupInfoUser() {
        txtUserName.setText(MainActivity.user.getUserName());
        Picasso.with(getContext()).load(MainActivity.user.getBanner().toString())
                .skipMemoryCache()
                .placeholder(R.drawable.banner).into(imgBanner);
        Picasso.with(getContext()).load(MainActivity.user.getAvatar().toString())
                .skipMemoryCache()
                .placeholder(R.drawable.person).into(imgAvatar);
    }

    public void SetupViewPager() {
        ArrayList<Fragment> arrayList = new ArrayList<>();
        userBaiHatFragment = new UserBaiHatFragment();
        userPlaylistFragment = new UserPlaylistFragment();
        arrayList.add(userBaiHatFragment);
        arrayList.add(new LibraryFragment());
        arrayList.add(userPlaylistFragment);
        ArrayList<String> title = new ArrayList<>();
        title.add("Yêu Thích");
        title.add("Trên Thiết Bị");
        title.add("Playlist");
        assert getFragmentManager() != null;
        adapter = new ViewPagerAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.setList(arrayList);
        adapter.setTitle(title);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void EventClick() {
        btnEdit.setOnClickListener(v -> {
            if (checkPermission()) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                SetupInfoUser();
            }
        });

        btnLogout.setOnClickListener(v -> {

            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
            dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Đăng Xuất");
            dialog.setIcon(R.drawable.ic_logout);
            dialog.setMessage("Bạn có chắc muốn đăng suất?");
            dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                SharedPreferences.Editor editor = LoginFragment.sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                startActivity(intent);
                Intent servivce = new Intent(getContext(), MusicService.class);
                getActivity().stopService(servivce);
                getActivity().finish();
            });

            dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        });

    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED) && (WRITE_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {

                    }
                }
            }
        }
    }


}