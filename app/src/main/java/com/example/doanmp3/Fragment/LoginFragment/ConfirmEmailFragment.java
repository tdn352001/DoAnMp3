package com.example.doanmp3.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmEmailFragment extends Fragment {
    View view;
    TextInputEditText edtCode;
    MaterialButton btnCf;
    TextView btnLogin;
    int Code;
    String Email, Username, Password;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_confirm_email, container, false);
        AnhXa();
        GetCode();
        EventClick();
        return view;
    }


    private void AnhXa() {
        edtCode = view.findViewById(R.id.edt_code_cf);
        btnCf = view.findViewById(R.id.btn_cf_code);
        btnLogin = view.findViewById(R.id.txt_cf_login);
    }

    private void GetCode() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Code = bundle.getInt("code");
            Email = bundle.getString("email");
            Username = bundle.getString("username");
            Password = bundle.getString("password");
            Log.e("BBBB", Code + " " +  Email + " " + Username);
        }
    }

    private void EventClick() {
        btnCf.setOnClickListener(v -> {
            String Vcode = edtCode.getText().toString().trim();
            if (Vcode.equals(Code + "")) {
                Register();
                btnCf.setClickable(false);
            } else {
                if (Vcode.equals("")) {
                    edtCode.setError("Vui Lòng Nhập Mã Xác Nhận");
                    Toast.makeText(getContext(), "Vui Lòng Nhập Mã Xác Nhận", Toast.LENGTH_SHORT).show();
                } else {
                    edtCode.setError("Mã Xác Nhận Không Chính Xác");
                    Toast.makeText(getContext(), "Mã Xác Nhận Không Chính Xác", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_confirmEmailFragment_to_loginFragment));

    }

    public void Register() {
        progressDialog = ProgressDialog.show(getContext(), "Đang thực hiện", "Vui Lòng chờ");
        DataService dataService = APIService.getUserService();
        Call<User> callback = dataService.RegisterUser(Email, Username, Password);
        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                btnCf.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_confirmEmailFragment_to_loginFragment);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                btnCf.setClickable(true);
                progressDialog.dismiss();
            }
        });

    }

}