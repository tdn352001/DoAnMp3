package com.example.doanmp3.Fragment.Login;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.doanmp3.Activity.MainActivity;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegisterFragment extends Fragment {

    View view;
    TextInputEditText edtEmail, edtUsername, edtPassword, edtVerifyPassword;
    MaterialButton btnRegister, btnLogin, btnLoginGg;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userReference;
    GoogleSignInClient googleSignInClient;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        InitComponents();
        InitDialog();
        InitFirebase();
        ConfigSignInMethod();
        HandleEvents();
        return view;
    }

    private void InitComponents() {
        edtEmail = view.findViewById(R.id.edt_email_register);
        edtUsername = view.findViewById(R.id.edt_username_register);
        edtPassword = view.findViewById(R.id.edt_password_register);
        edtVerifyPassword = view.findViewById(R.id.edt_confirm_password_register);
        btnRegister = view.findViewById(R.id.btn_register);
        btnLogin = view.findViewById(R.id.btn_navigate_login);
        btnLoginGg = view.findViewById(R.id.btn_login_google);
    }

    private void InitFirebase() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users");
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
        btnRegister.setOnClickListener(v -> HandleRegisterEvent());
        btnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        btnLoginGg.setOnClickListener(v -> LoginWithGoogle());
    }

    private void HandleRegisterEvent() {
        String email = Objects.requireNonNull(edtEmail.getText()).toString();
        String username = Objects.requireNonNull(edtUsername.getText()).toString();
        String password = Objects.requireNonNull(edtPassword.getText()).toString();
        String verifyPassword = Objects.requireNonNull(edtVerifyPassword.getText()).toString();


        if (validateNewAccount(email, username, password, verifyPassword)) {
            CheckUserExists(email, password, username);
        }
    }

    private boolean validateNewAccount(String email, String username, String password, String verifyPassword) {
        if (email.equals("")) {
            Toast.makeText(getContext(), getString(R.string.missing_email), Toast.LENGTH_SHORT).show();
            edtEmail.setError(getString(R.string.missing_email));
            edtEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            edtEmail.setError(getString(R.string.invalid_email));
            edtEmail.requestFocus();
            return false;
        }

        if (username.equals("")) {
            Toast.makeText(getContext(), getString(R.string.missing_username), Toast.LENGTH_SHORT).show();
            edtUsername.setError(getString(R.string.missing_username));
            edtUsername.requestFocus();
            return false;
        }

        if (username.length() < 6) {
            Toast.makeText(getContext(), getString(R.string.not_enough_length_username), Toast.LENGTH_SHORT).show();
            edtUsername.setError(getString(R.string.not_enough_length_username));
            edtUsername.requestFocus();
            return false;
        }

        if (password.equals("")) {
            Toast.makeText(getContext(), getString(R.string.missing_password), Toast.LENGTH_SHORT).show();
            edtPassword.setError(getString(R.string.missing_password));
            edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), getString(R.string.not_enough_length_password), Toast.LENGTH_SHORT).show();
            edtPassword.setError(getString(R.string.not_enough_length_password));
            edtPassword.requestFocus();
            return false;
        }

        if (!password.equals(verifyPassword)) {
            Toast.makeText(getContext(), getString(R.string.pasword_not_matching), Toast.LENGTH_SHORT).show();
            edtVerifyPassword.setError(getString(R.string.pasword_not_matching));
            edtVerifyPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void CheckUserExists(String email, String password, String username) {
        progressDialog.show();
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size() != 0) {
                edtEmail.requestFocus();
                edtEmail.setError(getString(R.string.email_already_used));
                Toast.makeText(getContext(), getString(R.string.email_already_used), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                Register(email, password, username);
            }
        }).addOnFailureListener(e -> progressDialog.dismiss());

    }

    private void Register(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            user.updateProfile(profileUpdates);
                            user.sendEmailVerification().addOnCompleteListener(task1 -> SaveUserToDatabase()).addOnFailureListener(e -> Toast.makeText(getContext(), getString(R.string.cant_send_verify_email), Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.register_failure), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", "Register ERROR: " + task.getException());
                    }

                });
    }

    private void SaveUserToDatabase() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;
        String photoUrl = user.getPhotoUrl() == null ? " " : user.getPhotoUrl().toString();
        User newUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(), photoUrl, " ", "");
        userReference.child(user.getUid()).setValue(newUser);
        Navigate();
    }

    private void Navigate(){
        Tools.hideSoftKeyBoard(getActivity());
        Navigation.findNavController(view).navigate(R.id.action_registerFragment2_to_registerSuccessfullyFragment);
        auth.signOut();
        progressDialog.dismiss();
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

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userReference.get().addOnCompleteListener(task -> {
            User dataUser = task.getResult().getValue(User.class);
            if(dataUser == null){
                String photoUrl = user.getPhotoUrl() == null ? " " : user.getPhotoUrl().toString();
                dataUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(), photoUrl, " ", "");
                userReference.setValue(dataUser);
            }
            NavigateToMainActivity();

        });
    }

    private void NavigateToMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
        Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
    }
}