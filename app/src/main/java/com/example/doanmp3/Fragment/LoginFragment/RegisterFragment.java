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

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class
RegisterFragment extends Fragment {

    ProgressDialog mProgressDialog;
    View view;
    TextInputEditText edtEmail, edtUsername, edtPassword, edtCpassword;
    MaterialButton btnRegister;
    TextView txtlogin;
    TextView a;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
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
        a = view.findViewById(R.id.txt_haveac);
    }

    private void EventClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setClickable(false);
                String email = edtEmail.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString();

                if (isValid()) {
                    mProgressDialog = ProgressDialog.show(getContext(), "Đang Thực Hiện", "Vui Lòng Chờ...", false, false);
                    Register(email, username, password);
                } else {
                    Log.e("BBB", " Form không hợp lệ");
                    btnRegister.setClickable(true);

                }
            }
        });

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });
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
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void Register(String email, String username, String password) {
        DataService dataService = APIService.getUserService();
        Call<User> callback = dataService.RegisterUser(email, username, password);

        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = (User) response.body();
                btnRegister.setClickable(true);
                mProgressDialog.dismiss();
                if (user.getIdUser().equals("-1")) {
                    {
                        edtEmail.setError("Email đã tồn tại");
                        Toast.makeText(getContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Đăng Ký Thành Công", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigateUp();
                }
                btnRegister.setClickable(true);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                btnRegister.setClickable(true);

            }
        });

    }
}