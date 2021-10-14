package com.example.doanmp3.NewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doanmp3.NewModel.Slide;
import com.example.doanmp3.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder>{

    Context context;
    ArrayList<Slide> slides;
    ItemClick itemClick;

    ArrayList<GradientDrawable> backgroundDrawables;
    ArrayList<Integer> colorNav;

    public SlideAdapter(Context context, ArrayList<Slide> slides, ItemClick itemClick) {
        this.context = context;
        this.slides = slides;
        this.itemClick = itemClick;
        backgroundDrawables = new ArrayList<>();
        colorNav = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null)
            context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_slider, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       Glide.with(context).asBitmap().load(slides.get(position).getThumbnail())
               .into(new CustomTarget<Bitmap>() {
                   @RequiresApi(api = Build.VERSION_CODES.N)
                   @Override
                   public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                       holder.thumbnail.setImageBitmap(resource);
                       AddBackgroundDrawable(position, resource);
                   }

                   @Override
                   public void onLoadCleared(@Nullable Drawable placeholder) {

                   }
               });
        holder.itemView.setOnClickListener(v -> itemClick.itemClick(position));
    }

    @Override
    public int getItemCount() {
        if(slides != null)
            return slides.size();
        return 0;
    }

    public GradientDrawable getBackgroundDrawables(int position) {
        if(backgroundDrawables == null || position >= backgroundDrawables.size()) {
            return null;
        }
        return backgroundDrawables.get(position);
    }

    public int getNavColors(int position) {
        if(colorNav == null || position >= colorNav.size()) {
            return 0;
        }
        return colorNav.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void AddBackgroundDrawable(int position, Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            if(palette != null){
                ArrayList<Integer> colorArraylist = new ArrayList<>();
                int color = 0;

                Palette.Swatch swatch1 = palette.getLightMutedSwatch();
                if(swatch1 != null){
                    color = swatch1.getRgb();
                }

                Palette.Swatch swatch2 = palette.getLightVibrantSwatch();
                if(swatch2 != null){
                    colorArraylist.add(swatch2.getRgb());
                    color = swatch2.getRgb();
                }

                Palette.Swatch swatch3 = palette.getMutedSwatch();
                if(swatch3 != null){
                    colorArraylist.add(swatch3.getRgb());
                }

                Palette.Swatch swatch4 = palette.getVibrantSwatch();
                if(swatch4 != null){
                    colorArraylist.add(swatch4.getRgb());
                }

                Palette.Swatch swatch5 = palette.getDominantSwatch();
                if(swatch5 != null){
                    colorArraylist.add(swatch5.getRgb());
                }

                Palette.Swatch swatch6 = palette.getDarkVibrantSwatch();
                if(swatch6 != null){
                    colorArraylist.add(swatch6.getRgb());
                }

                ArrayList<Integer> colorSlides = new ArrayList<>();
                colorSlides.addAll(colorArraylist);
                colorSlides.addAll(colorArraylist);
                colorArraylist.clear();
                Log.e("EEE", "" + colorSlides.size());

                int[] colorsSlide = colorSlides.stream().mapToInt(i -> i).toArray();
                for(int i = 0; i < colorsSlide.length; i++){
                    colorsSlide[i] = ColorUtils.blendARGB(colorsSlide[i], context.getColor(R.color.tutu), 0.3f);
                    colorsSlide[i] = brighter( colorsSlide[i], 0.1f);
                }

                GradientDrawable gradientDrawableSlide = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colorsSlide);

                color = ColorUtils.blendARGB(color, context.getColor(R.color.tutu), 0.5f);
                color = ColorUtils.setAlphaComponent(color, 36);
                backgroundDrawables.add( gradientDrawableSlide);
                colorNav.add(color);
            }
        });
    }


    //#E8DDE4

    static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView thumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.slide_thumbnail);
        }
    }

    public interface ItemClick {
        void itemClick(int position);
    }

    private int  brighter(int color, float factor) {
        float[] hsv = new float[3];
        color = ColorUtils.setAlphaComponent(color, 100);
        Color.colorToHSV(color, hsv);
        hsv[1] *= (1f + factor);
        hsv[2] *= (1f + factor);
        color = Color.HSVToColor(hsv);
        return color;
    }
}
