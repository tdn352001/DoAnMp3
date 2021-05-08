package com.example.doanmp3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.io.IOException;
import java.text.SimpleDateFormat;

import me.relex.circleindicator.CircleIndicator;

public class PlayActivity extends AppCompatActivity {

    Toolbar toolbar;

    SeekBar seekBar;
    TextView txtCurrent, txtTotal;
    ImageButton btnLoop, btnRandom, btnPre, btnNext, btnPlay;
    CircleIndicator indicator;
    MediaPlayer mediaPlayer;
    BaiHat baiHat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        AnhXa();
        GetIntent();
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("baihat")){
            baiHat = intent.getParcelableExtra("baihat");
            new PlayMp3().execute(baiHat.getLinkBaiHat());
        }
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_play);

        seekBar = findViewById(R.id.seekbar_time);
        txtCurrent = findViewById(R.id.current_time);
        txtTotal= findViewById(R.id.total_time);
        btnLoop = findViewById(R.id.btn_loop);
        btnNext = findViewById(R.id.btn_next);
        btnPre = findViewById(R.id.btn_prev);
        btnPlay = findViewById(R.id.btn_play);
        btnRandom = findViewById(R.id.btn_random);
        indicator = findViewById(R.id.circle_play);


    }


    class PlayMp3 extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            try {
            mediaPlayer= new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });


            mediaPlayer.setDataSource(s);
            mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            TimeSong();
        }
    }

    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }


}