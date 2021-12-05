package com.example.doanmp3.Service;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Activity.SystemActivity.PlaySongsActivity;
import com.example.doanmp3.Context.Data.AudioThumbnail;
import com.example.doanmp3.Context.Data.UserData;
import com.example.doanmp3.Dialog.BottomDialog;
import com.example.doanmp3.Models.Object;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tools {

    /**
     * @param radius 1..25
     * @return new bitmap
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
        if (null == bitmap) return null;
        if (radius <= 0) radius = 1f;
        if (radius > 25) radius = 25f;

        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }


    /**
     * @param bmp        input bitmap
     * @param contrast   0..10 1 is default
     * @param brightness -255..255 0 is default
     * @return new bitmap
     */
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bitmap;
    }

    public static Bitmap increaseSaturation(Bitmap bitmap, float settingSat) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] mapSrcColor = new int[w * h];
        int[] mapDestColor = new int[w * h];
        float[] pixelHSV = new float[3];
        bitmap.getPixels(mapSrcColor, 0, w, 0, 0, w, h);
        int index = 0;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {

                Color.colorToHSV(mapSrcColor[index], pixelHSV);
                pixelHSV[1] = pixelHSV[1] + settingSat;
                if (pixelHSV[1] < 0.0f) {
                    pixelHSV[1] = 0.0f;
                } else if (pixelHSV[1] > 1.0f) {
                    pixelHSV[1] = 1.0f;
                }
                mapDestColor[index] = Color.HSVToColor(pixelHSV);
                index++;
            }
        }
        return Bitmap.createBitmap(mapDestColor, w, h, Bitmap.Config.ARGB_8888);
    }


    /*
     * Hue (0 .. 360) Saturation (0...1) Value (0...1)
     */

    /**
     * @param src        bitmap
     * @param settingHue (0 .. 360)
     * @param settingSat Saturation (0...1)
     * @param settingVal (0...1)
     * @return new bitmap
     */
    public static Bitmap updateHSV(Bitmap src, float settingHue, float settingSat, float settingVal) {

        int w = src.getWidth();
        int h = src.getHeight();
        int[] mapSrcColor = new int[w * h];
        int[] mapDestColor = new int[w * h];

        float[] pixelHSV = new float[3];

        src.getPixels(mapSrcColor, 0, w, 0, 0, w, h);

        int index = 0;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {

                Color.colorToHSV(mapSrcColor[index], pixelHSV);

                pixelHSV[0] = pixelHSV[0] + settingHue;
                if (pixelHSV[0] < 0.0f) {
                    pixelHSV[0] = 0.0f;
                } else if (pixelHSV[0] > 360.0f) {
                    pixelHSV[0] = 360.0f;
                }

                pixelHSV[1] = pixelHSV[1] + settingSat;
                if (pixelHSV[1] < 0.0f) {
                    pixelHSV[1] = 0.0f;
                } else if (pixelHSV[1] > 1.0f) {
                    pixelHSV[1] = 1.0f;
                }

                pixelHSV[2] = pixelHSV[2] + settingVal;
                if (pixelHSV[2] < 0.0f) {
                    pixelHSV[2] = 0.0f;
                } else if (pixelHSV[2] > 1.0f) {
                    pixelHSV[2] = 1.0f;
                }
                mapDestColor[index] = Color.HSVToColor(pixelHSV);

                index++;
            }
        }

        return Bitmap.createBitmap(mapDestColor, w, h, Bitmap.Config.ARGB_8888);

    }

    public static Bitmap resizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int ratio) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (ratio < 0) ratio = 0;
        if (ratio > 100) ratio = 100;
        float scale = (float) (ratio / 100.0);
        int x = (int) (width * (1 - scale) / 2);
        int y = (int) (height * (1 - scale) / 2);
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);
        return Bitmap.createBitmap(bitmap, x, y, newWidth, newHeight);
    }


    public static String encodeStringToMd5(String string) {
        StringBuilder hashText = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(string.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashText.toString();
    }

    public static void hideSoftKeyBoard(FragmentActivity context) {
        if (context == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            View view = context.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void CreateAndShowForeground(View view, int alpha) {
        Drawable drawableDim = view.getForeground();
        if (drawableDim == null) {
            drawableDim = ResourcesCompat.getDrawable(view.getResources(), R.drawable.foreground_dim, null);
            view.setForeground(drawableDim);
        }

        if (drawableDim != null) {
            drawableDim.setAlpha(alpha);
        }
    }

    public static boolean isObjectInObjects(Object object, ArrayList<Object> objects) {
        if (objects == null || object == null)
            return false;

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getId().equals(object.getId()))
                return true;
        }

        return false;
    }

    public static SpannableStringBuilder SetTextStyle(String text, int Style) {
        SpannableStringBuilder textStyle = new SpannableStringBuilder(text);
        textStyle.setSpan(new StyleSpan(Style), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return textStyle;
    }

    public static int ConvertDpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int ConvertSpToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static String CalculateTimeAgo(Context context, Date past) {
        Date now = new Date();
        long milliSecondAgo = now.getTime() - past.getTime();
        long minuteAgo = TimeUnit.MILLISECONDS.toMinutes(milliSecondAgo);

        if (minuteAgo < 60) {
            return minuteAgo + context.getApplicationContext().getString(R.string.minute_ago);
        }

        long hourAgo = TimeUnit.MINUTES.toHours(minuteAgo);
        if (hourAgo < 24) {
            return hourAgo + context.getApplicationContext().getString(R.string.hour_ago);
        }

        long dayAgo = TimeUnit.MINUTES.toDays(minuteAgo);
        if (dayAgo < 30) {
            return dayAgo + context.getApplicationContext().getString(R.string.day_ago);
        }

        long monthAgo = dayAgo / 30;
        return monthAgo + context.getApplicationContext().getString(R.string.month_ago);
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static boolean isSongInSongs(Song song, ArrayList<Song> songs) {
        if (song == null || songs == null || songs.size() == 0)
            return false;
        for (int i = 0; i < songs.size(); i++) {
            Song temp = songs.get(i);
            if (song.isAudio() || song.getId().equals("-1")) {
                if (temp.getLink().equals(song.getLink()))
                    return true;
            } else {
                if (temp.getId().equals(song.getId()))
                    return true;
            }
        }
        return false;
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void FeatureIsImproving(Context context) {
        if (context == null)
            return;

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
        dialog.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_background));
        dialog.setTitle(R.string.notification);
        dialog.setMessage(R.string.feature_improving);
        dialog.setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }


    public static void NavigateToPlayActivity(Context context, ArrayList<Song> songs, int position, boolean random) {
        if (context == null || songs == null || songs.size() <= position)
            return;

        Intent intent = new Intent(context, PlaySongsActivity.class);
        intent.putExtra("songs", songs);
        intent.putExtra("position", position);
        intent.putExtra("random", random);
        context.startActivity(intent);
    }

    public static BottomDialog DialogOptionSongDefault(Context context, Song song) {
        if (context == null || song == null)
            return null;

        BottomDialog dialog = new BottomDialog(context);
        dialog.setContentView(R.layout.dialog_options_song);

        CircleImageView imgThumbnail = dialog.findViewById(R.id.thumbnail_song);
        TextView tvSong = dialog.findViewById(R.id.tv_name_song);
        TextView tvSingers = dialog.findViewById(R.id.tv_name_singer);
        MaterialButton btnLove = dialog.findViewById(R.id.btn_love);
        MaterialButton btnPlay = dialog.findViewById(R.id.btn_play_song);
        MaterialButton btnAddQueue = dialog.findViewById(R.id.btn_add_queue);

        Glide.with(context).load(song.getThumbnail()).into(imgThumbnail);
        tvSong.setText(song.getName());
        tvSingers.setText(song.getAllSingerNames());

        boolean isLoved = UserData.isLoveSong(song);
        int icon = isLoved ? R.drawable.ic_love : R.drawable.ic_hate;
        int loveText = isLoved ? R.string.unlike : R.string.favourite;
        btnLove.setIconResource(icon);
        btnLove.setText(loveText);

        btnLove.setOnClickListener(v -> {
            UserData.AddOrRemoveSong(song, true);
            dialog.dismiss();
        });

        btnPlay.setOnClickListener(v -> {
            ArrayList<Song> songs = new ArrayList<>();
            songs.add(song);
            NavigateToPlayActivity(context, songs, 0, false);
            dialog.dismiss();
        });

        btnAddQueue.setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicService.class);
            intent.putExtra("song", song);
            context.startService(intent);
            dialog.dismiss();
        });

        return dialog;
    }

    public static Bitmap GetBitmapOfAudio(Context context, Song song) {
        if (context == null || song == null) {
            return null;
        }
        if (song.getId().equals("-1") || song.isAudio()) {
            int resId;
            try {
                resId = Integer.parseInt(song.getThumbnail());
            } catch (Exception e) {
                resId = AudioThumbnail.getRandomThumbnail();
            }
            return BitmapFactory.decodeResource(context.getResources(), resId);
        }

        return BitmapFactory.decodeResource(context.getResources(), R.drawable.music2);
    }


}
