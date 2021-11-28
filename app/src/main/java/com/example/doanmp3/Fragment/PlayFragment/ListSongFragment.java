package com.example.doanmp3.Fragment.PlayFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Adapter.PlaySongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.util.ArrayList;


public class ListSongFragment extends Fragment {

    PlaySongAdapter playSongAdapter;
    RecyclerView recyclerView;
    ArrayList<BaiHat> arrayList;
    TextView txtCaSi, txtBaiHat, txtDanhMuc;

    public ListSongFragment(ArrayList<BaiHat> arrayList) {
        this.arrayList = arrayList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);

        txtBaiHat = view.findViewById(R.id.txt_info_song);
        txtCaSi = view.findViewById(R.id.txt_info_casi);
        txtDanhMuc = view.findViewById(R.id.txt_info_danhmuc);
        recyclerView = view.findViewById(R.id.rv_listsong_fragment);
        playSongAdapter = new PlaySongAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(playSongAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        try {
            setDanhMuc();
        } catch (Exception e) {
            txtDanhMuc.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    public void setInfoBaiHat(String BaiHat, String CaSi) {
        txtBaiHat.setText("Bài Hát:  " + BaiHat);
        txtCaSi.setText("Ca Sĩ:  " + CaSi);
    }

    @SuppressLint("SetTextI18n")
    public void setDanhMuc() {
        if (!(DanhSachBaiHatActivity.category.equals("")))
            txtDanhMuc.setText(DanhSachBaiHatActivity.category + ":  " + DanhSachBaiHatActivity.TenCategoty);

    }

    public void ChuyenBai(int index) {
        playSongAdapter.changeRowSelected(index);
    }


}