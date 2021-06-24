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

import org.jetbrains.annotations.NotNull;

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
                if (edtEmail.getText().toString().trim().equals("")) {
                    edtEmail.setError("Vui Lòng Nhập Email");
                    Toast.makeText(getContext(), "Vui Lòng Nhập Email", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Không Tìm Thấy Email Trên Hệ Thống", Toast.LENGTH_SHORT).show();
                            } else {
                                Random random = new Random();
                                int code = random.nextInt(99999 - 10000) + 10000;
                                Email email = new Email();
                                if (email.Sendto(edtEmail.getText().toString(), "Quên Mật Khẩu", "Mã Xác Nhận Của Bạn là:" + code)) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Mã Xác Nhận Đã Được Gửi Đến Email Của Bạn", Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("iduser", iduser);
                                    bundle.putInt("code", code);
                                    Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_confirmCodeFragment, bundle);
                                } else {
                                    Toast.makeText(getContext(), "Hệ Thống Lỗi! Vui Lòng Thử Lại Sau", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "Hệ Thống Lỗi! Vui Lòng Thử Lại Sau", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });

                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
