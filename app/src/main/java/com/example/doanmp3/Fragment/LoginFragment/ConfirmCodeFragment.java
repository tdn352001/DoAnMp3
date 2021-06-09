package com.example.doanmp3.Fragment.LoginFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ConfirmCodeFragment extends Fragment {

   View view;
   TextInputEditText edtCode;
   MaterialButton btnCf;
   TextView btnLogin;
   int Code;
   String IdUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_confirm_code, container, false);
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
        if(bundle != null){
            Code = bundle.getInt("code");
            IdUser = bundle.getString("iduser");
        }
    }

    private void EventClick() {
        btnCf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Vcode = edtCode.getText().toString();
                if(Vcode.equals(Code +"")){
                    Bundle bundle = new Bundle();
                    bundle.putString("iduser", IdUser);
                    Navigation.findNavController(view).navigate(R.id.action_confirmCodeFragment_to_changePasswordFragment, bundle);
                }
                else{
                    if(Vcode.equals(""))
                        edtCode.setError("Vui Lòng Nhập Mã Xác Nhận");
                    else
                        edtCode.setError("Mã Xác Nhận Không Chính Xác");
                }
            }
        });
    }
}