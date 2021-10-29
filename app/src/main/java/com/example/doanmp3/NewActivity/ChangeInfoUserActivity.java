package com.example.doanmp3.NewActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeInfoUserActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 12345;
    Toolbar toolbar;
    TextInputEditText edtDisplayName, edtDescription;
    MaterialButton btnBanner, btnAvatar, btnSave, btnCancel;
    CircleImageView imgAvatar;
    RoundedImageView imgBanner;
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference userReference;
    StorageReference storageRef;
    StorageReference avatarStoRef;
    StorageReference bannerStoRef;
    Uri avatarUri;
    Uri bannerUri;
    int countProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_user);
        InitComponents();
        InitFirebase();
        InitDialog();
        SetupInfoUser();
        HandleEvents();
    }

    private void InitComponents() {
        toolbar = findViewById(R.id.toolbar_edit_info_user);
        edtDisplayName = findViewById(R.id.edt_display_name);
        edtDescription = findViewById(R.id.edt_description_user);
        btnAvatar = findViewById(R.id.btn_edt_avatar_user);
        imgAvatar = findViewById(R.id.img_edit_avatar);
        btnBanner = findViewById(R.id.btn_edt_banner_user);
        imgBanner = findViewById(R.id.img_edit_banner);
        btnSave = findViewById(R.id.btn_save_change);
        btnCancel = findViewById(R.id.btn_cancel_change);
        countProcess = 0;
    }

    private void InitFirebase() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("users");
        storageRef = FirebaseStorage.getInstance().getReference("users");
        avatarStoRef = storageRef.child("avatars");
        bannerStoRef = storageRef.child("banners");
    }

    private void InitDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
    }


    @SuppressLint("CheckResult")
    private void SetupInfoUser() {
        if (user == null) {
            finish();
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        edtDisplayName.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(imgAvatar);
    }

    private void HandleEvents() {
        btnAvatar.setOnClickListener(v -> HandleSelectAvatar());
        imgAvatar.setOnClickListener(v -> HandleSelectAvatar());
        btnSave.setOnClickListener(v -> HandleSaveChanges());
    }

    private void HandleSelectAvatar() {
        if (CheckPermissions()) {
            OpenGallery();
        } else {
            RequestPermissions();
        }
    }

    private void HandleSaveChanges() {

        // Validate
        String displayName = Objects.requireNonNull(edtDisplayName.getText()).toString().trim();
        if (displayName.isEmpty()) {
            edtDisplayName.requestFocus();
            Toast.makeText(this, getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show();
            return;
        }
        if (displayName.length() < 6) {
            Toast.makeText(this, getString(R.string.not_enough_length_username), Toast.LENGTH_SHORT).show();
            edtDisplayName.setError(getString(R.string.not_enough_length_username));
            edtDisplayName.requestFocus();
            return;
        }

        progressDialog.show();
        SaveUserAuth();
        SaveUserDatabase();
    }


    private void SaveUserAuth() {
        if (avatarUri != null)
            UploadPhoto();
        else
            UploadUserProfile(null);
    }


    private void UploadPhoto() {
        avatarStoRef.child(user.getUid()).putFile(avatarUri)
                .addOnSuccessListener(taskSnapshot -> {
                    avatarStoRef.child(user.getUid()).getDownloadUrl().
                            addOnCompleteListener(task -> UploadUserProfile(task.getResult()));
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ChangeInfoUserActivity.this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                });
    }


    private void UploadUserProfile(Uri photoUri) {
        UserProfileChangeRequest.Builder profileBuilder = new UserProfileChangeRequest.Builder();
        String displayName = Objects.requireNonNull(edtDisplayName.getText()).toString().trim();
        profileBuilder.setDisplayName(displayName);
        if (photoUri != null)
            profileBuilder.setPhotoUri(photoUri);

        user.updateProfile(profileBuilder.build()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ChangeInfoUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangeInfoUserActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                Log.e("EEE", "Update Failed: " + Objects.requireNonNull(task.getException()).getMessage());
            }
            CheckProgress();
        });
    }


    private void SaveUserDatabase() {
        CheckProgress();
    }

    private void CheckProgress() {
        countProcess++;
        if (countProcess >= 2) {
            countProcess = 0;
            progressDialog.dismiss();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }


    private boolean CheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void RequestPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenGallery();
            }
        }
    }

    private final ActivityResultLauncher<Intent> avatarPicResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent intent = result.getData();

            if (intent != null && intent.getData() != null) {
                avatarUri = intent.getData();
                Glide.with(ChangeInfoUserActivity.this).load(avatarUri).into(imgAvatar);
            }
        }
    });

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        avatarPicResult.launch(Intent.createChooser(intent, "SELECT A PICTURE"));
    }

}