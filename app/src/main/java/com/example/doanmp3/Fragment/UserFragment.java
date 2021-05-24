package com.example.doanmp3.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Activity.UserInfoActivity;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    View view;
    TextView txtUserName;
    MaterialButton btnEdit;
    ImageView imgBanner;
    CircleImageView imgAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_user, container, false);
        AnhXa();
        Setup();
        EventClick();
        return view;
    }



    private void AnhXa() {
        btnEdit = view.findViewById(R.id.btn_edit_user_info);
        imgAvatar = view.findViewById(R.id.img_user_avatar);
        imgBanner = view.findViewById(R.id.img_user_banner);
        txtUserName = view.findViewById(R.id.txt_username);

    }
    private void Setup() {
        txtUserName.setText(MainActivity.user.getUserName());
        Picasso.with(getContext()).load(MainActivity.user.getBanner().toString()).into(imgBanner);
        Picasso.with(getContext()).load(MainActivity.user.getAvatar().toString()).into(imgAvatar);
    }

    private void EventClick() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}