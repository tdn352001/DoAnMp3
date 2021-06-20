package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.doanmp3.Fragment.LoginFragment.RegisterFragment;
import com.example.doanmp3.Fragment.UserFragment.UserFragment;
import com.example.doanmp3.Model.Md5;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialButton btnBanner, btnAvatar, txtUserName, txtEmail, txtPassword;
    CircleImageView imgAvatar;
    RoundedImageView imgBanner;
    int RequestAvatar = 123;
    int RequestBanner = 321;
    User user;
    Bitmap bitmap;
    Md5 md5 = new Md5();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        Setup();
        EventClick();
    }


    private void AnhXa() {
        btnAvatar = findViewById(R.id.btn_edt_avatar_user);
        btnBanner = findViewById(R.id.btn_edt_banner_user);
        imgAvatar = findViewById(R.id.img_edit_avater);
        imgBanner = findViewById(R.id.img_edit_banner);
        txtUserName = findViewById(R.id.txt__edit_username);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        toolbar = findViewById(R.id.toolbar_edit_infouser);
        user = MainActivity.user;
    }

    @SuppressLint("SetTextI18n")
    private void Setup() {
        txtEmail.setText("Email: " + user.getEmail().toString());
        txtUserName.setText("Tên: " + user.getUserName());


        Glide.with(this).load(MainActivity.user.getBanner().toString())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.banner).into(imgBanner);
        Picasso.with(this).load(MainActivity.user.getAvatar().toString())
                .skipMemoryCache().error(R.drawable.person)
                .placeholder(R.drawable.person).into(imgAvatar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chỉnh Sửa Thông Tin");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void EventClick() {
        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageUpload(RequestAvatar);
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageUpload(RequestAvatar);
            }
        });

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageUpload(RequestBanner);
            }
        });

        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageUpload(RequestBanner);
            }
        });

        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenChangeUserNameDialog();
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenChangeEmailDialog();
            }
        });

        txtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenChangePassworDialog();
            }
        });

    }

    private void SelectImageUpload(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                if (requestCode == RequestAvatar) {
                    imgAvatar.setImageBitmap(bitmap);
                    uploadimage("Avatar");
                } else {
                    imgBanner.setImageBitmap(bitmap);
                    uploadimage("Banner");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadimage(String Category) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);



        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.UploadPhoto(encodedImage, user.getEmail() + Category);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = (String) response.body();
                if (Category.equals("Avatar")) {
                    UserFragment.imgAvatar.setImageBitmap(bitmap);
                } else {
                    UserFragment.imgBanner.setImageBitmap(bitmap);
                }
                Toast.makeText(UserInfoActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // ĐỔI EMAIL

    private void OpenChangeEmailDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        TextInputEditText edtEmail, edtPassword;
        MaterialButton btnConfirm, btnCancel;

        edtEmail = dialog.findViewById(R.id.edt_email_change);
        edtPassword = dialog.findViewById(R.id.edt_email_change_checkpass);
        btnConfirm = dialog.findViewById(R.id.btn_cofirm_change_email);
        btnCancel = dialog.findViewById(R.id.btn_cancel_change_email);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.equals("")) {
                    edtEmail.setError("Email Trống");
                } else {
                    if (!RegisterFragment.EmailIsValid(email)) {
                        edtEmail.setError("Email không hợp lệ");
                    } else {
                        if (email.equals(user.getEmail())) {
                            edtEmail.setError("Email cũ");
                        } else {
                            if (password.equals("")) {
                                edtPassword.setError("Vui lòng nhập mật khẩu");
                            } else {
                                if (!md5.endcode(password).equals(user.getPassword())) {
                                    edtPassword.setError("Mật khẩu không đúng");
                                } else {
                                    DataService dataService = APIService.getUserService();
                                    Call<String> callback = dataService.ChangeEmail(email, user.getIdUser());
                                    callback.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String result = (String) response.body();
                                            if (result.equals("Thanh Cong")) {
                                                dialog.dismiss();
                                                Toast.makeText(UserInfoActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                                                txtEmail.setText("Email: "+email);
                                                MainActivity.user.setEmail(email);
                                                user = MainActivity.user;
                                            } else {
                                                if (result.equals("Ton Tai")) {
                                                    Toast.makeText(UserInfoActivity.this, "Email Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    dialog.dismiss();
                                                    Toast.makeText(UserInfoActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            dialog.dismiss();
                                            Toast.makeText(UserInfoActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });

        dialog.show();

    }

    //ĐỔi USERNAME

    private void OpenChangeUserNameDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_username);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        TextInputEditText edtUserName, edtPassword;
        MaterialButton btnConfirm, btnCancel;
        edtUserName = dialog.findViewById(R.id.edt_user_change);
        edtPassword = dialog.findViewById(R.id.edt_username_change_checkpass);
        btnConfirm = dialog.findViewById(R.id.btn_cofirm_change_username);
        btnCancel = dialog.findViewById(R.id.btn_cancel_change_username);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.length() < 6) {
                    edtUserName.setError("Username có ít nhất 6 kí tự");
                } else {
                    if (!md5.endcode(password).equals(user.getPassword())) {
                        edtPassword.setError("Mật Khẩu Không Đúng");
                    } else {
                        DataService dataService = APIService.getUserService();
                        Call<String> callback = dataService.ChangeUserName(username, user.getIdUser());
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = (String) response.body();
                                if (result.equals("Thanh Cong")) {
                                    dialog.dismiss();
                                    Toast.makeText(UserInfoActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                                    txtUserName.setText("Tên: " + username);
                                    UserFragment.txtUserName.setText(username);
                                    MainActivity.user.setUserName(username);
                                    user = MainActivity.user;
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(UserInfoActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(UserInfoActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        dialog.show();

    }

    // ĐỔI PASSWORD

    private void OpenChangePassworDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password_change);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        TextInputEditText edtNewPassword, edtPassword, edtCPassword;
        MaterialButton btnConfirm, btnCancel;
        edtPassword = dialog.findViewById(R.id.edt_password_change);
        edtNewPassword = dialog.findViewById(R.id.edt_password_change_newpass);
        edtCPassword = dialog.findViewById(R.id.edt_password_change_cpass);
        btnConfirm = dialog.findViewById(R.id.btn_cofirm_change_password);
        btnCancel = dialog.findViewById(R.id.btn_cancel_change_password);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString();
                String npassword = edtNewPassword.getText().toString();
                String cpassword = edtCPassword.getText().toString();

                if (!md5.endcode(password).equals(user.getPassword())) {
                    edtPassword.setError("Mật Khẩu Không Đúng");
                } else {
                    if (npassword.length() < 6)
                        edtNewPassword.setError("Mật Khẩu Có Tối Thiểu 6 Kí Tự");
                    else {
                        if (!npassword.equals(cpassword))
                            edtCPassword.setError("Mật Khẩu Không Trùng Khớp");
                        else {
                            DataService dataService = APIService.getUserService();
                            Call<String> callback = dataService.ChangePassword(npassword, user.getIdUser());
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = (String) response.body();
                                    if (result.equals("Thanh Cong")) {
                                        dialog.dismiss();
                                        Toast.makeText(UserInfoActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                                        MainActivity.user.setPassword(md5.endcode(npassword));
                                        user = MainActivity.user;
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(UserInfoActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d("BBB", result);
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(UserInfoActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
        dialog.show();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}