package com.example.doanmp3.Fragment.Login;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.NewActivity.MainActivity;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class LoginFragment extends Fragment {

    View view;
    TextInputEditText edtEmail, edtPassword;
    MaterialButton btnForgotPassword, btnLogin, btnRegister, btnLoginGg;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        InitComponents();
        InitDialog();
        ConfigSignInMethod();
        HandleEvents();
        return view;
    }

    private void InitComponents() {
        edtEmail = view.findViewById(R.id.edt_email_login);
        edtPassword = view.findViewById(R.id.edt_password_login);
        btnForgotPassword = view.findViewById(R.id.forget_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);
        btnLoginGg = view.findViewById(R.id.btn_login_google);
    }

    private void InitDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.please_wait));
    }


    private void ConfigSignInMethod() {
        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    private void HandleEvents() {
        view.setOnClickListener(v -> Tools.hideSoftKeyBoard(getActivity()));
        btnLogin.setOnClickListener(v -> HandleLoginEvent());
        btnRegister.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment2));
        btnForgotPassword.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_forgotPasswordFragment2));
        btnLoginGg.setOnClickListener(v -> LoginWithGoogle());
    }


    private void HandleLoginEvent() {
        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();
        if (validateAccount(email, password)) {
            Login(email, password);
        }
    }

    private boolean validateAccount(String email, String password) {
        if (email.equals("")) {
            edtEmail.setError(getString(R.string.missing_email));
            Toast.makeText(getContext(), getString(R.string.missing_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.equals("")) {
            edtPassword.setError(getString(R.string.missing_password));
            Toast.makeText(getContext(), getString(R.string.missing_email), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void Login(String email, String password) {
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    CheckEmailVerified(user);
                }
                progressDialog.dismiss();
            } else {
                HandleErrorLogin(email);
            }
        });
    }

    private void CheckEmailVerified(FirebaseUser user) {
        if (user.isEmailVerified()) {
            NavigateToMainActivity();
            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        } else {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireContext());
            dialog.setTitle(getString(R.string.verify_email));
            dialog.setMessage(getString(R.string.verify_email_to_login));
            dialog.setNegativeButton(R.string.send_email, (dialog1, which) ->
                    user.sendEmailVerification()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                    Toast.makeText(getContext(), getString(R.string.verify_email_to_login), Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("ERROR","sendEmailVerification: " + e.getMessage());
                                Toast.makeText(getContext(), R.string.cant_send_verify_email, Toast.LENGTH_SHORT).show();
                            }));
            dialog.setPositiveButton(R.string.ok, (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        }
    }


    private void HandleErrorLogin(String email) {
        edtPassword.requestFocus();
        edtPassword.setError(getString(R.string.email_password_invalid));
        Toast.makeText(getContext(), getString(R.string.email_password_invalid), Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();

//        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
//            if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size() != 0) {
//
//
//                edtEmail.setError(getString(R.string.email_already_used));
//                Toast.makeText(getContext(), getString(R.string.user_doesnt_have_password), Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(getContext(), getString(R.string.email_password_invalid), Toast.LENGTH_SHORT).show();
//            }
//            progressDialog.dismiss();
//        }).addOnFailureListener(e -> progressDialog.dismiss());
    }

    private final ActivityResultLauncher<Intent> LoginGoogleResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d("EEE", "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Log.w("EEE", "Google sign in failed ", e);
                    }
                }
            });


    private void LoginWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        LoginGoogleResult.launch(signInIntent);
    }

    @SuppressLint("ShowToast")
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar.make(view, "Đăng nhập thành công", Snackbar.LENGTH_SHORT).setAction(R.string.ok,null).show();
                NavigateToMainActivity();
            } else {
                Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "firebaseAuthWithGoogle: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void NavigateToMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }



}