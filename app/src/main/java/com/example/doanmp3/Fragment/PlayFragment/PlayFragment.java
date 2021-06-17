package com.example.doanmp3.Fragment.PlayFragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlayFragment extends Fragment {

   View view;
   TextView textView;
   ImageButton HideActivity;
   TextView titleBaiHat, titleCaSi;
   CircleImageView circleImageView;
   public static ObjectAnimator objectAnimator;
    public PlayFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play, container, false);
        circleImageView = view.findViewById(R.id.dianhac);
        textView = view.findViewById(R.id.txt_song_casi_play_fragment);
        titleBaiHat = view.findViewById(R.id.txt_title_baihat);
        titleCaSi = view.findViewById(R.id.txt_title_casi);
        HideActivity = view.findViewById(R.id.img_finish_play);
        objectAnimator= ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setStartDelay(1);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setupStartValues();
        objectAnimator.start();

        HideActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }


   public void setHinh(String link, boolean isAudio) {
       if(!isAudio)
        Picasso.with(getContext()).load(link).into(circleImageView);
       else
           circleImageView.setImageResource(R.drawable.img_disknhac);

    }
    public void setContent(String BaiHat, String CaSi){
         textView.setText(BaiHat + "  -  " + CaSi);
         titleBaiHat.setText(BaiHat);
         titleCaSi.setText(CaSi);
    }

}