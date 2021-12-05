package com.example.doanmp3.Context.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioThumbnail {
    static ArrayList<Integer> thumbnails;
    static ArrayList<Bitmap> bitmaps;

    private static void InitThumbnail(){
        if (thumbnails == null) {
            thumbnails = new ArrayList<>();
            thumbnails.add(R.drawable.img_song_0);
            thumbnails.add(R.drawable.img_song_1);
            thumbnails.add(R.drawable.img_song_2);
            thumbnails.add(R.drawable.img_song_3);
            thumbnails.add(R.drawable.img_song_4);
        }
    }

    public static Integer getRandomThumbnail() {
        InitThumbnail();
        Random random = new Random();
        int position = random.nextInt(thumbnails.size());
        return thumbnails.get(position);
    }

    public static void InitBitmaps(Context context){
        if(bitmaps != null)
            return;
        InitThumbnail();
        bitmaps = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for(int i = 0; i < thumbnails.size(); i++){
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), thumbnails.get(i));
                bitmaps.add(bitmap);
            }
        });
    }

    public static Bitmap getRandomThumbnailBitmap(Context context, int position){
        if(bitmaps == null || bitmaps.size() <= position){
            return null;
        }
        return bitmaps.get(position);
    }
}