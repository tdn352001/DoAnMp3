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

import com.example.doanmp3.Activity.SystemActivity.MainActivity;
import com.example.doanmp3.Context.Constant.FirebaseRef;
import com.example.doanmp3.Models.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class ForgotPasswordFragment extends Fragment {

    View view;
    TextInputEditText edtEmail;
    MaterialButton btnConfirm, btnRegister, btnLoginGg;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        auth = FirebaseAuth.getInstance();
        InitComponents();
        ConfigSignInMethod();
        InitDialog();
        HandleEvents();
        return view;
    }

    private void InitComponents() {
        edtEmail = view.findViewById(R.id.edt_email_forgot_password);
        btnConfirm = view.findViewById(R.id.btn_confirm);
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
        btnConfirm.setOnClickListener(v -> HandleSendPassword());
        btnRegister.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment2);
        });
        btnLoginGg.setOnClickListener(v -> LoginWithGoogle());
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
        }).addOnFailureListener(e -> Log.e("ERROR", "ForgotPassword Failed: " + e.getMessage()));
    }

    private void SendResetPasswordMail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment2_to_changePasswordSuccessfullyFragment))
                .addOnFailureListener(e -> Log.e("ERROR","SendResetPasswordMail:" + e.getMessage()));
    }

    private void LoginWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        LoginGoogleResult.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> LoginGoogleResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Log.e("EEE", "Google sign in failed ", e);
                    }
                }
            });

    @SuppressLint("ShowToast")
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CheckUserExistInDatabase();
            } else {
                Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "firebaseAuthWithGoogle: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }


    private void CheckUserExistInDatabase(){
        progressDialog.show();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.USERS).child(user.getUid());
        userReference.get().addOnCompleteListener(task -> {
            User dataUser = task.getResult().getValue(User.class);
            if(dataUser == null){
                String photoUrl = user.getPhotoUrl() == null ? " " : user.getPhotoUrl().toString();
                dataUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(), photoUrl, " ", "");
                userReference.setValue(dataUser);
            }
            NavigateToMainActivity();
            Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
        });
    }

    private void NavigateToMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
        Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
    }
}