package com.example.doanmp3.Fragment.UserPlaylist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Dialog.BottomDialog;
import com.example.doanmp3.Dialog.CustomDialog;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Activity.DetailUserPlaylistActivity;
import com.example.doanmp3.NewAdapter.UserPlaylistAdapter;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserPlaylistFragment extends Fragment {

    View view;
    RelativeLayout btnAdd;
    RecyclerView rvPlaylist;
    ArrayList<Playlist> playlists;
    UserPlaylistAdapter adapter;
    private int connectAgainst;
    FirebaseUser user;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_playlist, container, false);
        InitComponents();
        HandleEvents();
        GetDataPlaylist();
        return view;
    }

    private void InitComponents() {
        btnAdd = view.findViewById(R.id.btn_add_playlist);
        rvPlaylist = view.findViewById(R.id.rv_user_playlist);
        connectAgainst = 0;
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait));
    }

    private void HandleEvents() {
        btnAdd.setOnClickListener(v -> OpenCreateDialog());
    }

    private void GetDataPlaylist() {
        if (user == null) return;
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getUserPlaylists(user.getUid());
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                if (playlists == null)
                    playlists = new ArrayList<>();
                SetUpRecycleView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                if (connectAgainst < 3) {
                    GetDataPlaylist();
                    connectAgainst++;
                    Log.e("ERROR", "GetDataPlaylist " + t.getMessage());
                }
            }

        });
    }

    private void SetUpRecycleView() {
        adapter = new UserPlaylistAdapter(getContext(), playlists, new OptionItemClick() {
            @Override
            public void onItemClick(int position) {
                Playlist playlist = playlists.get(position);
                Intent intent = new Intent(getActivity(), DetailUserPlaylistActivity.class);
                intent.putExtra("playlist", playlist);
                startActivity(intent);
            }

            @Override
            public void onOptionClick(int position) {
                ShowOptionPlaylistDialog(position);
            }
        });
        rvPlaylist.setAdapter(adapter);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvPlaylist.setLayoutAnimation(layoutAnimation);
    }

    private void OpenCreateDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_playlist);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        EditText edtName;
        MaterialButton btnAdd, btnCancel;

        edtName = dialog.findViewById(R.id.edt_name);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnAdd = dialog.findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            if (name.equals("")) {
                Toast.makeText(getActivity(), R.string.please_enter_name, Toast.LENGTH_SHORT).show();
                return;
            }

            if (CheckNamePlaylistExists(name)) {
                Toast.makeText(getActivity(), R.string.the_name_already_exist, Toast.LENGTH_SHORT).show();
                return;
            }

            AddNewPlaylist(name);
            dialog.dismiss();

        });

        dialog.show();
    }


    private boolean CheckNamePlaylistExists(String name) {
        if (playlists == null || playlists.size() == 0)
            return false;

        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(name))
                return true;
        }

        return false;
    }

    private void ShowOptionPlaylistDialog(int position) {
        Playlist playlist = playlists.get(position);
        if (playlist == null) return;

        BottomDialog bottomSheetDialog = new BottomDialog(requireActivity());
        bottomSheetDialog.setContentView(R.layout.dialog_option_playlist);
        // Init Component
        CircleImageView imgThumbnail = bottomSheetDialog.findViewById(R.id.thumbnail_playlist);
        TextView tvName = bottomSheetDialog.findViewById(R.id.tv_name);
        MaterialButton btnDelete = bottomSheetDialog.findViewById(R.id.btn_delete);
        MaterialButton btnRename = bottomSheetDialog.findViewById(R.id.btn_rename);
        // Set up component
        Glide.with(requireContext()).load(playlist.getThumbnail()).error(R.drawable.playlist_viewholder).into(imgThumbnail);
        tvName.setText(playlist.getName());

        // Event
        btnRename.setOnClickListener(v -> {
            OpenRenameDialog(playlist.getId(), playlist.getName(), position);
            bottomSheetDialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            OpenDeleteDialog(playlist.getId(), position);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void OpenRenameDialog(String id, String oldName, int position) {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_playlist);
        Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
            dialog.setCancelable(true);
        }
        dialog.setCancelable(true);

        TextView tvTitle;
        EditText edtName;
        MaterialButton btnAdd, btnCancel;

        tvTitle = dialog.findViewById(R.id.dialog_title);
        edtName = dialog.findViewById(R.id.edt_name);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnAdd = dialog.findViewById(R.id.btn_ok);

        tvTitle.setText(R.string.change_name);
        edtName.setText(oldName);
        edtName.requestFocus();
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            if (name.equals("")) {
                Toast.makeText(getActivity(), R.string.please_enter_name, Toast.LENGTH_SHORT).show();
                return;
            }

            if (CheckNamePlaylistExists(name)) {
                Toast.makeText(getActivity(), R.string.the_name_already_exist, Toast.LENGTH_SHORT).show();
                return;
            }

            RenamePlaylist(id, name, position);
            dialog.dismiss();

        });

        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void OpenDeleteDialog(String id, int position){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setBackground(getResources().getDrawable(R.drawable.rounded_background));
        dialogBuilder.setTitle(R.string.delete_playlist);
        dialogBuilder.setMessage(R.string.are_you_sure);
        dialogBuilder.setIcon(R.drawable.ic_warning);
        dialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        dialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> DeleteUserPlaylist(id, position));
        dialogBuilder.show();
    }

    private void AddNewPlaylist(String name) {
        progressDialog.show();
        DataService dataService = APIService.getService();
        Call<Playlist> callback = dataService.addUserPlaylist(user.getUid(), name);
        callback.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                Playlist newPlaylist = response.body();
                if (newPlaylist != null) {
                    playlists.add(newPlaylist);
                    adapter.notifyItemInserted(playlists.size());

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("EEE", t.getMessage());
            }
        });
    }

    private void RenamePlaylist(String id, String name, int position) {
        progressDialog.show();
        DataService dataService = APIService.getService();
        Call<Playlist> callback = dataService.updateUserPlaylist(id, name);
        callback.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                Playlist newPlaylist = response.body();
                if (newPlaylist != null) {
                    playlists.remove(position);
                    playlists.add(position, newPlaylist);
                    adapter.notifyItemChanged(position);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void DeleteUserPlaylist(String id, int position) {
        if(playlists == null) return;
        progressDialog.show();
        DataService dataService = APIService.getService();
        Call<Playlist> callback = dataService.deleteUserPlaylist(id);
        callback.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                playlists.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, playlists.size());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}