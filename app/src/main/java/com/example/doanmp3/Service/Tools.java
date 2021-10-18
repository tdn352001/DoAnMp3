package com.example.doanmp3.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
     * @param src bitmap
     * @param settingHue (0 .. 360)
     * @param settingSat Saturation (0...1)
     * @param settingVal (0...1)
     * @return new bitmap
     */
    public static Bitmap updateHSV(Bitmap src, float settingHue, float settingSat,float settingVal) {

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
}
