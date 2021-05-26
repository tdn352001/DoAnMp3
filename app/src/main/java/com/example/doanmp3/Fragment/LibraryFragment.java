package com.example.doanmp3.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AudioAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ModelAudio;
import com.example.doanmp3.R;

import java.util.ArrayList;
import java.util.Collections;

public class LibraryFragment extends Fragment {
    public static final int PERMISSION_READ = 0;
    public static final int PERMISSION_WRITE = 1;
    RecyclerView recyclerView;
    AudioAdapter adapter;
    ArrayList<ModelAudio> audios;
    ArrayList<BaiHat> arrayList;
    public LibraryFragment() {
        // Required empty public constructor
    }
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        audios = new ArrayList<>();
        arrayList = new ArrayList<>();
        if(checkPermission())
            getAudioFiles();

        return view;
    }
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED) && (WRITE_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {
                        getAudioFiles();
                    }
                }
            }
        }
    }


    public void getAudioFiles() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                ModelAudio modelAudio = new ModelAudio(title,duration, artist,  Uri.parse(url) );
                audios.add(modelAudio);


                // sua
                BaiHat baiHat = new BaiHat();
                baiHat.setCaSi(Collections.singletonList(artist));
                baiHat.setIdBaiHat("-1");
                baiHat.setTenBaiHat(title);
                baiHat.setLinkBaiHat(Uri.parse(url).toString());
                baiHat.setHinhBaiHat("R.drawable.img_disknhac");
                arrayList.add(baiHat);

            } while (cursor.moveToNext());
        }


        adapter = new AudioAdapter(getActivity(), audios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);



    }
}