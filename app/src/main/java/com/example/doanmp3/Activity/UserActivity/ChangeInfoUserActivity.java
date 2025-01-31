package com.example.doanmp3.Activity.UserActivity;

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
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Activity.SystemActivity.BaseActivity;
import com.example.doanmp3.Context.Constant.FirebaseRef;
import com.example.doanmp3.Models.User;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeInfoUserActivity extends BaseActivity {

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
    ValueEventListener valueEventListener;
    StorageReference storageRef;
    StorageReference avatarStoRef;
    StorageReference bannerStoRef;
    User dataUser;
    Uri avatarUri;
    Uri bannerUri;
    int countProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_user);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        InitComponents();
        InitFirebase();
        InitDialog();
        GetDataUser();
        SetToolBar();
        SetupInfoUser();
        HandleEvents();
    }

    private void SetToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.edit_info_user));
        toolbar.setNavigationOnClickListener(v -> finish());
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
        userReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.USERS).child(user.getUid());
        storageRef = FirebaseStorage.getInstance().getReference("users");
        avatarStoRef = storageRef.child("avatars");
        bannerStoRef = storageRef.child("banners");
    }

    private void InitDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
    }

    private void GetDataUser() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataUser = snapshot.getValue(User.class);
                if (dataUser == null){
                    String photoUrl = user.getPhotoUrl() == null ? " " : user.getPhotoUrl().toString();
                    dataUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(), photoUrl, " ", "");
                }
                edtDescription.setText(dataUser.getDescription());
                Glide.with(getApplicationContext())
                        .load(dataUser.getBannerUri())
                        .error(R.drawable.banner)
                        .into(imgBanner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userReference.addValueEventListener(valueEventListener);
    }


    @SuppressLint("CheckResult")
    private void SetupInfoUser() {
        if (user == null) {
            finish();
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        edtDisplayName.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl())
                .error(R.drawable.person)
                .into(imgAvatar);

    }

    private void HandleEvents() {
        btnAvatar.setOnClickListener(v -> HandleSelectAvatar());
        imgAvatar.setOnClickListener(v -> HandleSelectAvatar());
        btnBanner.setOnClickListener(v -> HandleSelectBanner());
        imgBanner.setOnClickListener(v -> HandleSelectBanner());
        btnSave.setOnClickListener(v -> HandleSaveChanges());
        btnCancel.setOnClickListener(v -> CancelEdit());
    }

    private void HandleSelectAvatar() {
        if (CheckPermissions()) {
            OpenGalleryToSelectAvatar();
        } else {
            RequestPermissions();
        }
    }

    private void HandleSelectBanner(){
        if (CheckPermissions()) {
            OpenGalleryToSelectBanner();
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
        //Save Firebase User Info
        SaveUserAuth();

        // Save User Info In Database
        SaveUserDatabase();
    }


    private void SaveUserAuth() {
        if (avatarUri != null)
            UploadAvatarPhoto();
        else
            UpdateUserProfile(null);
    }

    private void UploadAvatarPhoto() {
        avatarStoRef.child(user.getUid()).putFile(avatarUri)
                .addOnSuccessListener(taskSnapshot -> avatarStoRef.child(user.getUid()).getDownloadUrl().
                        addOnCompleteListener(task -> UpdateUserProfile(task.getResult())))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ChangeInfoUserActivity.this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                });
    }

    private void UpdateUserProfile(Uri photoUri) {
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
                Log.e("ERROR", "Update Failed: " + Objects.requireNonNull(task.getException()).getMessage());
            }
            CheckProgress();
        });
    }

    private void SaveUserDatabase() {
        if(bannerUri != null){
            UploadBannerPhoto();
        }else{
            UpdateUserTable(null);
        }
    }

    private void UploadBannerPhoto() {
        bannerStoRef.child(user.getUid()).putFile(bannerUri)
                .addOnSuccessListener(taskSnapshot -> bannerStoRef.child(user.getUid()).getDownloadUrl().
                        addOnCompleteListener(task -> UpdateUserTable(task.getResult())))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ChangeInfoUserActivity.this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                });
    }

    private void UpdateUserTable(Uri result) {
        String displayName = Objects.requireNonNull(edtDisplayName.getText()).toString().trim();
        String description = Objects.requireNonNull(edtDescription.getText()).toString().trim();
        String avatarUrl = user.getPhotoUrl() == null ? dataUser.getAvatarUri() : user.getPhotoUrl().toString();

        if(result != null)
            dataUser.setBannerUri(result.toString());

        dataUser.setName(displayName);
        dataUser.setDescription(description);
        dataUser.setAvatarUri(avatarUrl);
        userReference.setValue(dataUser);
        CheckProgress();
    }


    private void CheckProgress() {
        countProcess++;
        if (countProcess >= 2) {
            countProcess = 0;
            setResult(Activity.RESULT_OK);
            //Update User Avatar In Database
            String avatarUrl = user.getPhotoUrl() == null ? dataUser.getAvatarUri() : user.getPhotoUrl().toString();
            userReference.child("avatarUri").setValue(avatarUrl);

            // Finish Progress
            progressDialog.dismiss();
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
                OpenGalleryToSelectAvatar();
            }else{
                Snackbar.make(imgAvatar, getString(R.string.request_perrmission), Snackbar.LENGTH_LONG)
                        .setAction(R.string.ok, v -> {})
                        .show();
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

    private void OpenGalleryToSelectAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        avatarPicResult.launch(Intent.createChooser(intent, "SELECT A PICTURE"));
    }

    private final ActivityResultLauncher<Intent> bannerPicResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent intent = result.getData();

            if (intent != null && intent.getData() != null) {
                bannerUri = intent.getData();
                Glide.with(ChangeInfoUserActivity.this).load(bannerUri).into(imgBanner);
            }
        }
    });

    private  void  OpenGalleryToSelectBanner(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        bannerPicResult.launch(Intent.createChooser(intent, "SELECT A PICTURE"));
    }

    private void CancelEdit(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle(getString(R.string.confirm));
        dialogBuilder.setMessage(R.string.change_not_save);
        dialogBuilder.setIcon(R.drawable.ic_warning);
        dialogBuilder.setPositiveButton(R.string.no, (dialog, which) -> {});
        dialogBuilder.setNegativeButton(R.string.yes, (dialog, which) -> finish());
        dialogBuilder.show();
    }

    @Override
    public void finish() {
        super.finish();
        userReference.removeEventListener(valueEventListener);
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}