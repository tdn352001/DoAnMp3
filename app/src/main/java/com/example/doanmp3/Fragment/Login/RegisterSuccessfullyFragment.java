package com.example.doanmp3.Fragment.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;

public class RegisterSuccessfullyFragment extends Fragment {

    View view;
    MaterialButton btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_successfully, container, false);

        btnLogin = view.findViewById(R.id.btn_navigate_login);

        btnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_registerSuccessfullyFragment_to_loginFragment2));

        return view;
    }
}