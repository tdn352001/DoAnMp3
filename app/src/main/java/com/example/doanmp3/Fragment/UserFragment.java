package com.example.doanmp3.Fragment;

import android.Manifest;
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

import com.example.doanmp3.Activity.LoginActivity;
import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Activity.UserBaiHatActivity;
import com.example.doanmp3.Activity.UserInfoActivity;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    View view;

    MaterialButton btnEdit, btnLogout;
    MaterialButton btnBaiHat, btnCaSi, btnPlaylist, btnAlbum;
    public static TextView txtUserName;
    public static ImageView imgBanner;
    public static CircleImageView imgAvatar;
    public static final int PERMISSION_READ = 0;
    public static final int PERMISSION_WRITE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        AnhXa();
        Setup();
        checkPermission();
        EventClick();

        return view;
    }


    private void AnhXa() {
        btnEdit = view.findViewById(R.id.btn_edit_user_info);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnBaiHat = view.findViewById(R.id.btn_user_baihat);
        btnPlaylist = view.findViewById(R.id.btn_user_playlists);
        btnCaSi = view.findViewById(R.id.btn_user_nghesi);
        btnAlbum = view.findViewById(R.id.btn_user_album);
        imgAvatar = view.findViewById(R.id.img_user_avatar);
        imgBanner = view.findViewById(R.id.img_user_banner);
        txtUserName = view.findViewById(R.id.txt_username);

    }

    public void Setup() {
        txtUserName.setText(MainActivity.user.getUserName());
        Picasso.with(getContext()).load(MainActivity.user.getBanner().toString()).into(imgBanner);
        Picasso.with(getContext()).load(MainActivity.user.getAvatar().toString()).into(imgAvatar);
    }

    private void EventClick() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                    Setup();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                SharedPreferences.Editor editor = LoginFragment.sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.commit();
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnBaiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserBaiHatActivity.class);
                startActivity(intent);
            }
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