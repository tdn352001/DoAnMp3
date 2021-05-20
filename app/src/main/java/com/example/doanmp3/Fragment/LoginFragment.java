package com.example.doanmp3.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.doanmp3.R;


public class LoginFragment extends Fragment {

    View view;
    EditText edtEmail, edtPassword;
    TextView txtRegister, txtForget;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        AnhXa();
        EventClick();
        return view;
    }

    private void AnhXa() {
        edtEmail = view.findViewById(R.id.edt_email_login);
        edtPassword = view.findViewById(R.id.edt_password_login);
        txtForget = view.findViewById(R.id.forget_password);
        txtRegister = view.findViewById(R.id.txt_register_login);
    }

    private void EventClick() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

    }
}