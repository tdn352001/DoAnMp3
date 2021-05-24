package com.example.doanmp3.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialButton btnBanner, btnAvatar;
    CircleImageView imgAvatar;
    RoundedImageView imgBanner;
    TextView txtUserName, txtEmail, txtPassword;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        AnhXa();
        Setup();
        EventClick();
    }



    private void AnhXa() {
        btnAvatar = findViewById(R.id.btn_edt_avatar_user);
        btnBanner = findViewById(R.id.btn_edt_banner_user);
        imgAvatar = findViewById(R.id.img_edit_avater);
        imgBanner = findViewById(R.id.img_edit_banner);
        txtUserName = findViewById(R.id.txt__edit_username);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        toolbar = findViewById(R.id.toolbar_edit_infouser);
        user = MainActivity.user;
    }

    private void Setup() {
        txtEmail.setText("Email: " + user.getEmail().toString());
        txtUserName.setText("Tên: "+user.getUserName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chỉnh Sửa Thông Tin");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void EventClick(){
        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        
    }
}