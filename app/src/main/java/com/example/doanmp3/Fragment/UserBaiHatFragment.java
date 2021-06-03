package com.example.doanmp3.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.AllSongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBaiHatFragment extends Fragment {

    View view;
    public RecyclerView recyclerView;
    public static TextView textView;
    public static AllSongAdapter adapter;
    public static ArrayList<BaiHat> arrayList;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_bai_hat, container, false);
        AnhXa();
        GetBaiHatYeuThich();
        return view;
    }


    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_user_baihat_yeuthich);
        textView = view.findViewById(R.id.txt_user_baihat_yeuthich);
        user = MainActivity.user;

    }

    public void SetupBaiHatYeuThich() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                textView.setVisibility(View.INVISIBLE);
                SetRecyclerView();
            } else
                textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void SetRecyclerView() {
        adapter = new AllSongAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void GetBaiHatYeuThich() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatYeuThich(user.getIdUser());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                SetupBaiHatYeuThich();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean checkLiked(String IdBaiHat) {

        if (arrayList != null && arrayList.size() > 0)
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getIdBaiHat().equals(IdBaiHat))
                    return true;
            }

        return false;
    }

    public static void BoThichBaiHat(String IdBaiHat) {
        if (arrayList != null && arrayList.size() > 0)
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    arrayList.remove(i);
                    adapter.notifyDataSetChanged();
                }
            }
    }
}