package com.example.doanmp3.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Adapter.PlaySongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.R;

import java.util.ArrayList;


public class ListSongFragment extends Fragment {

    PlaySongAdapter playSongAdapter;
    RecyclerView recyclerView;
    ArrayList<BaiHat> arrayList;
    MediaPlayer mediaPlayer;
    TextView txtCaSi, txtBaiHat, txtDanhMuc;

    public ListSongFragment(ArrayList<BaiHat> arrayList, MediaPlayer mediaPlayer) {
        this.arrayList = arrayList;
        this.mediaPlayer = mediaPlayer;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);

        txtBaiHat=view.findViewById(R.id.txt_info_song);
        txtCaSi =view.findViewById(R.id.txt_info_casi);
        txtDanhMuc = view.findViewById(R.id.txt_info_danhmuc);
        recyclerView= view.findViewById(R.id.rv_listsong_fragment);
        playSongAdapter = new PlaySongAdapter( getContext(), arrayList, mediaPlayer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(playSongAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        try{
            setDanhMuc();
        }
        catch (Exception e) {
            txtDanhMuc.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    public void setInfoBaiHat(String BaiHat, String CaSi){
        txtBaiHat.setText("Bài Hát:  " + BaiHat);
        txtCaSi.setText("Ca Sĩ:  " + CaSi);
    }

    public void setDanhMuc(){
        if(! (DanhSachBaiHatActivity.category ==""))
            txtDanhMuc.setText(DanhSachBaiHatActivity.category + ":  " + DanhSachBaiHatActivity.TenCategoty);

    }

    public void ChuyenBai(int index){
        playSongAdapter.changeRowSelected(index);
    }


}