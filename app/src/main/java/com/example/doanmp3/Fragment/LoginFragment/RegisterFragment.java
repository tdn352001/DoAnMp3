package com.example.doanmp3.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
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


public class RegisterFragment extends Fragment {

    ProgressDialog progressDialog;
    View view;
    TextInputEditText edtEmail, edtUsername, edtPassword, edtCpassword;
    MaterialButton btnRegister;
    TextView txtlogin;
    TextView haveacount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AnhXa();
        EventClick();
        return view;
    }

    private void AnhXa() {
        edtEmail = view.findViewById(R.id.edt_email_register);
        edtUsername = view.findViewById(R.id.edt_username_register);
        edtPassword = view.findViewById(R.id.edt_password_register);
        edtCpassword = view.findViewById(R.id.edt_cpassword_register);
        btnRegister = view.findViewById(R.id.btn_register);
        txtlogin = view.findViewById(R.id.txt_register_login);
        haveacount = view.findViewById(R.id.txt_haveac);
    }

    private void EventClick() {
        btnRegister.setOnClickListener(v -> {
            btnRegister.setClickable(false);
            String email = edtEmail.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString();

            if (isValid()) {
                progressDialog = ProgressDialog.show(getContext(), "Đang Thực Hiện", "Vui Lòng Chờ...", true, true);
                CheckExisit(email, username, password);
            } else {
                btnRegister.setClickable(true);

            }
        });

        txtlogin.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }


    private boolean isValid() {
        if (edtEmail.getText().toString().trim().equals("")) {
            edtEmail.setError("Email trống");
            Toast.makeText(getContext(), "Email trống", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!EmailIsValid(edtEmail.getText().toString())) {
                edtEmail.setError("Email không hợp lệ");
                Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (edtUsername.getText().toString().trim().equals("")) {
            edtUsername.setError("Username trống");
            Toast.makeText(getContext(), "Username trống", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (edtUsername.getText().toString().length() < 5) {
                edtUsername.setError("Username có tối thiếu 6 kí tự");
                Toast.makeText(getContext(), "Username có tối thiếu 6 kí tự", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (edtPassword.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Vui Lòng Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
            edtPassword.setError("Vui Lòng Nhập Mật Khẩu");
            return false;
        } else {
            if (edtPassword.getText().toString().length() < 6) {
                Toast.makeText(getContext(), "Mật Khẩu phải có tối thiểu 6 kí tự", Toast.LENGTH_SHORT).show();
                edtPassword.setError("Mật Khẩu phải có tối thiểu 6 kí tự");
                return false;
            } else {
                if (edtCpassword.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Vui lòng xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
                    edtCpassword.setError("Vui lòng xác nhận mật khẩu");
                    return false;
                } else {
                    if (!edtCpassword.getText().toString().equals(edtPassword.getText().toString())) {
                        edtCpassword.setError("Mật Khẩu Không Trùng Khớp");
                        Toast.makeText(getContext(), "Mật Khẩu Không Trùng Khớp", Toast.LENGTH_SHORT).show();
                        edtCpassword.setText("");
                        return false;
                    }
                }
            }
        }


        return true;
    }

    public static boolean EmailIsValid(String email) {
        if (email == null)
            return false;

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void CheckExisit(String email, String username, String password) {
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.CheckEmailExist(email);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnRegister.setClickable(true);
                progressDialog.dismiss();
                String exist = response.body();
                if (exist.equals("F")) {
                    CreateCode(email, username, password);
                } else {
                    edtEmail.setError("Email đã tồn tại");
                    Toast.makeText(getContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                btnRegister.setClickable(true);
                progressDialog.dismiss();
            }
        });
    }

    private void CreateCode(String email, String username, String password) {
        Random random = new Random();
        int code = random.nextInt(99999 - 10000) + 10000;
        Email mEmail = new Email();
        if (mEmail.Sendto(edtEmail.getText().toString(), "Hoàn Tất Đăng Ký", "Mã Xác Nhận Tài Khản MP Của Bạn là:" + code)) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Mã Xác Nhận Đã Được Gửi Đến Email Của Bạn", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putInt("code", code);
            bundle.putString("email", email);
            bundle.putString("username", username);
            bundle.putString("password", password);
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_confirmEmailFragment, bundle);
        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Hệ Thống Lỗi! Vui Lòng Thử Lại Sau", Toast.LENGTH_LONG).show();
        }
    }

}