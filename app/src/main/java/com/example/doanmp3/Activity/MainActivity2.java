package com.example.doanmp3.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ImageFilterView img = findViewById(R.id.img);

        Glide.with(this).asBitmap()
                .load("http://hotgirl18.net/wp-content/uploads/2021/11/hot-girl-trung-quoc-cosplay-hau-gai-phong-cach-sexy-hinh-9.jpg")
                .into(new CustomTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Toast.makeText(MainActivity2.this, "Lấy được ảnh", Toast.LENGTH_SHORT).show();
                        img.setImageBitmap(Tools.blurBitmap(MainActivity2.this, resource, 25));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.e("EEE", "ALO123");
                        Toast.makeText(MainActivity2.this, "Chịu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}