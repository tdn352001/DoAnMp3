package com.example.doanmp3.Fragment.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ForgotPasswordFragment extends Fragment {

    View view;
    TextInputEditText edtEmail;
    MaterialButton btnConfirm, btnRegister, btnLoginGg;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password2, container, false);
        auth = FirebaseAuth.getInstance();
        InitComponents();
        HandleEvents();
        return view;
    }

    private void InitComponents() {
        edtEmail = view.findViewById(R.id.edt_email_forgot_password);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnRegister = view.findViewById(R.id.btn_register);
        btnLoginGg = view.findViewById(R.id.btn_login_google);
    }

    private void HandleEvents() {
        btnConfirm.setOnClickListener(v -> HandleSendPassword());
        btnRegister.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment2);
        });
        view.setOnClickListener(v -> Tools.hideSoftKeyBoard(getActivity()));
    }

    private void HandleSendPassword() {
        // Check user exist
        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size() != 0) {
                SendResetPasswordMail(email);
            } else {
                edtEmail.requestFocus();
                edtEmail.setError(getString(R.string.email_not_found));
            }
        }).addOnFailureListener(e -> Log.e("EEE", "ForgotPassword Failed: " + e.getMessage()));
    }

    private void SendResetPasswordMail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment2_to_changePasswordSuccessfullyFragment);
                }).addOnFailureListener(e -> {
                    Log.e("EEE", e.getMessage());
                });
    }


}