package com.example.doanmp3.Fragment.HomeFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmp3.Activity.AllCategoryActivity;
import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.AlbumAdapter;
import com.example.doanmp3.Adapter.CategoryAdapter;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {

    View view;
    TextView txt;
    RecyclerView recyclerView;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        txt = view.findViewById(R.id.txt_category_fragment);
        recyclerView = view.findViewById(R.id.rv_category);
        GetData();

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllCategoryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callback = dataService.GetRandomCDTL();
        callback.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                ArrayList<ChuDeTheLoai> albums = (ArrayList<ChuDeTheLoai>) response.body();
                CategoryAdapter adapter = new CategoryAdapter(getActivity(), albums);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                MainActivity.LoadingComplete();

            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });
    }
}