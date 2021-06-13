package com.example.doanmp3.Fragment.UserFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.AddBaiHatActivity;
import com.example.doanmp3.Activity.DetailUserPlaylistActivity;
import com.example.doanmp3.Adapter.AddBaiHatAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.util.ArrayList;


public class Added_AddFragment extends Fragment {

    View view;
    public static RelativeLayout textView;
    RecyclerView recyclerView;
    public static int position;
    public static AddBaiHatAdapter adapter;
    public static ArrayList<BaiHat> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_added__add, container, false);
        AnhXa();
        GetBaiHatAdded();
        return view;
    }


    private void AnhXa() {
        textView = view.findViewById(R.id.txt_noinfo_add_added);
        recyclerView = view.findViewById(R.id.rv_add_added);
    }

    public void SetRV() {
        adapter = new AddBaiHatAdapter(getContext(), arrayList, true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void GetBaiHatAdded() {
        arrayList = DetailUserPlaylistActivity.arrayList;
        if (arrayList != null) {
            adapter = new AddBaiHatAdapter(getContext(), arrayList, true);
            SetRV();
            if (arrayList.size() > 0) {
                textView.setVisibility(View.INVISIBLE);
                return;
            }
        }
        textView.setVisibility(View.VISIBLE);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 10);
//                if(DetailUserPlaylistActivity.arrayList != null){
//                    arrayList = DetailUserPlaylistActivity.arrayList;
//                    SetRV();
//                    if (arrayList.size() > 0) {
//                        textView.setVisibility(View.INVISIBLE);
//                    }else
//                        textView.setVisibility(View.VISIBLE);
//
//                    handler.removeCallbacks(this);
//                }
//            }
//        }, 10);
    }

    public static boolean checkAddedBefore(String IdBaiHat) {
        position = -1;
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++)
                if (arrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    Log.e("BBBB", "true");
                    position = i;
                    return true;
                }
        }
        return false;
    }

    public static void Remove(String IdBaiHat) {
        if (checkAddedBefore(IdBaiHat)) {
            if (position != -1) {
                arrayList.remove(position);
                adapter.notifyItemRemoved(position);
                AddBaiHatActivity.ChangeStatus(IdBaiHat);
                if (arrayList.size() == 0)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.GONE);
            }
        }
    }

    public static void Add(BaiHat baiHat) {
        arrayList.add(baiHat);
        adapter.notifyDataSetChanged();
        AddBaiHatActivity.ChangeStatus(baiHat.getIdBaiHat());
        if (adapter.getItemCount() > 0)
            textView.setVisibility(View.GONE);
        else
            textView.setVisibility(View.VISIBLE);

    }


//    public void GetBaiHat() {
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 200);
//                if (DetailUserPlaylistActivity.arrayList != null) {
//                    arrayList = DetailUserPlaylistActivity.arrayList;
//                    if (arrayList.size() > 0) {
//                        adapter = new AddBaiHatAdapter(getContext(), arrayList, true);
//                        recyclerView.setAdapter(adapter);
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//                        recyclerView.setLayoutManager(linearLayoutManager);
//                        textView.setVisibility(View.INVISIBLE);
//                        return;
//                    } else
//                        textView.setVisibility(View.VISIBLE);
//
//                    handler.removeCallbacks(this);
//                }
//            }
//        };
//        handler.postDelayed(runnable, 200);
//    }


}