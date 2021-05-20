package com.example.doanmp3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView Register;
    EditText username, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        AnhXa();
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataService dataService = APIService.getUserService();
//                 Call<User> callback = dataService.GetUser(username.getText().toString(), password.getText().toString());
//
//               callback.enqueue(new Callback<User>() {
//                   @Override
//                   public void onResponse(Call<User> call, Response<User> response) {
//                       User user = response.body();
//                       if (user != null)
//                           Toast.makeText(LoginActivity.this, "Đăng Nhập thành công", Toast.LENGTH_SHORT).show();
//                       else
//                           Toast.makeText(LoginActivity.this, "tức", Toast.LENGTH_SHORT).show();
//                   }
//
//                   @Override
//                   public void onFailure(Call<User> call, Throwable t) {
//                       Toast.makeText(LoginActivity.this, "Tức lần 2", Toast.LENGTH_SHORT).show();
//                   }
//               });
//            }
//        });
    }

//    private void AnhXa() {
//        Register = findViewById(R.id.txt_register_btn);
//        username = findViewById(R.id.email_login);
//        password = findViewById(R.id.password_login);
//        login = findViewById(R.id.btn_login);
//    }
}