package com.example.doanmp3.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {


    View view;
    TextInputEditText edtPW, edtCF;
    MaterialButton btnCf;
    TextView btnLogin;
    String IdUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        AnhXa();
        GetUser();
        EventClick();
        return view;
    }

    private void AnhXa() {
        edtPW = view.findViewById(R.id.edt_change_password);
        edtCF = view.findViewById(R.id.edt_password_confirm);
        btnCf = view.findViewById(R.id.btn_changelogin);
        btnLogin = view.findViewById(R.id.txt_change_login);
    }

    private void GetUser() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            IdUser = bundle.getString("iduser");
        }
    }

    private void EventClick() {
        btnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_loginFragment));

        btnCf.setOnClickListener(v -> {
            String password = edtPW.getText().toString();
            String cpassword = edtCF.getText().toString();
            if (password.equals("")) {
                {
                    edtPW.setError("Vui Lòng Nhập Mật Khẩu Mới!");
                    Toast.makeText(getContext(), "Vui Lòng Nhập Mật Khẩu Mới!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (password.length() < 6) {
                    edtPW.setError("Mật Khẩu Có Tối Thiểu 6 Kí Tự!");
                    Toast.makeText(getContext(), "Mật Khẩu Có Tối Thiểu 6 Kí Tự!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(cpassword)) {
                        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Đang Thực Hiện", "Vui Lòng Chờ....!!!", false, false);
                        DataService dataService = APIService.getUserService();
                        Call<String> callback = dataService.ChangePassword(password, IdUser);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = (String) response.body();
                                if (result.equals("Thanh Cong"))
                                    Toast.makeText(getContext(), "Thay Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_loginFragment);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressDialog.dismiss();
                                Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_loginFragment);
                                Toast.makeText(getContext(), "Lỗi Mạng! Vui Lòng Thử Lại", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        edtCF.setText("");
                        edtCF.setError("Mật Khẩu Không Trùng Khớp");
                        Toast.makeText(getContext(), "Mật Khẩu Không Trùng Khớp", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}