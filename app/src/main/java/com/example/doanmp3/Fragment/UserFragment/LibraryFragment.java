package com.example.doanmp3.Fragment.UserFragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
    RecyclerView recyclerView;
    AudioAdapter adapter;
    ArrayList<ModelAudio> audios;
    ArrayList<BaiHat> arrayList;

    @RequiresApi(api = Build.VERSION_CODES.Q)
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

        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
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


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void getAudioFiles() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                ModelAudio modelAudio = new ModelAudio(title, artist,  Uri.parse(url) );
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