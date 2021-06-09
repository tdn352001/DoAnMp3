package com.example.doanmp3.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.Model.Email;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordFragment extends Fragment {
    View view;
    TextInputEditText edtEmail;
    TextView login;
    MaterialButton btnCofirm;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        AnhXa();
        EventClick();
        return view;
    }


    private void AnhXa() {
        edtEmail = view.findViewById(R.id.edt_email_fp);
        login = view.findViewById(R.id.txt_fp_login);
        btnCofirm = view.findViewById(R.id.btn_fp_login);
    }

    private void EventClick() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        btnCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().equals("")) {
                    edtEmail.setError("Vui Lòng Nhập Email");
                } else {
                    progressDialog = ProgressDialog.show(getContext(), "Đang Kiểm Tra", "Vui Lòng Chờ.....!", false, false);
                    DataService dataService = APIService.getUserService();
                    Call<String> callback = dataService.ForgotPassword(edtEmail.getText().toString());
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String iduser = response.body();
                            if (iduser == null) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Không Tìm THấy Email Trên Hệ Thống", Toast.LENGTH_SHORT).show();
                            } else {
                                Random random = new Random();
                                int a = random.nextInt(99999 - 10000) + 10000;
                                Email email = new Email();
                                if (email.Sendto(edtEmail.getText().toString(), "Quên Mật Khẩu", "Mật Khẩu Mới Của Bạn Là:" + a)) {
                                    Call<String> callchangepassword = dataService.ChangePassword(a + "", iduser);
                                    callchangepassword.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            progressDialog.dismiss();
                                            Navigation.findNavController(view).navigateUp();
                                            String result = (String) response.body();
                                            if (result.equals("Thanh Cong"))
                                                Toast.makeText(getContext(), "Mật Khẩu Đã Được Gửi Về Email Của Bạn", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Hệ Thống Lỗi! Vui Lòng Thử Lại Sau", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                 }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
