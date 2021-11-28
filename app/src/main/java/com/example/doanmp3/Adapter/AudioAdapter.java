package com.example.doanmp3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ModelAudio;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    Context context;
    ArrayList<ModelAudio> audios;
    ArrayList<BaiHat> arrayList;

    public AudioAdapter(Context context, ArrayList<ModelAudio> audios) {
        this.context = context;
        this.audios = audios;
        arrayList = new ArrayList<>();
        if (audios.size() > 0) {
            for (int i = 0; i < audios.size(); i++) {
                arrayList.add(audios.get(i).convertBaiHat());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_audio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(audios.get(position).getaudioTitle());
        holder.artist.setText(audios.get(position).getaudioArtist());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayNhacActivity.class);
            intent.putExtra("mangbaihat", arrayList);
            intent.putExtra("position", position);
            intent.putExtra("audio", true);
            DanhSachBaiHatActivity.category = "Playlist";
            DanhSachBaiHatActivity.TenCategoty = "Bài hát trên thiết bị";
            context.startActivity(intent);
        });

        holder.btnOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnOptions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SetupPopUpMenu(position, popupMenu);
                popupMenu.show();
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SetupPopUpMenu(int position, PopupMenu popupMenu) {
        popupMenu.getMenuInflater().inflate(R.menu.menu_option_audio, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.RIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_to_queue) {
                StartService(position);
            } else {
                OpenDiaLogDelete(position);
            }
            return true;
        });
    }

    private void StartService(int Pos) {
        Intent intent = new Intent(context, com.example.doanmp3.Service.MusicService.class);
        if (MusicService.arrayList == null) {
            ArrayList<BaiHat> baiHats = new ArrayList<>();
            baiHats.add(arrayList.get(Pos));
            intent.putExtra("mangbaihat", baiHats);
            intent.putExtra("audio", false);
            intent.putExtra("pos", 0);
            intent.putExtra("recent", false);
            DanhSachBaiHatActivity.category = "Playlist";
            DanhSachBaiHatActivity.TenCategoty = "Ngẫu Nhiên";
            context.startService(intent);
            Toast.makeText(context, "Đã Thêm", Toast.LENGTH_SHORT).show();
        } else {
            if (!MusicService.arrayList.contains(arrayList.get(Pos))) {
                MusicService.arrayList.add(arrayList.get(Pos));
                Toast.makeText(context, "Đã Thêm", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "Đã tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void OpenDiaLogDelete(int position) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
        dialog.setBackground(context.getDrawable(R.drawable.rounded_background));
        dialog.setTitle("Xóa");
        dialog.setIcon(R.drawable.ic_logout);
        dialog.setMessage("Bạn có chắc muốn xóa mục này?");
        dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
            try {
                DeleteFile(position);
            } catch (Exception e) {
                Toast.makeText(context, "Xóa Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private void DeleteFile(int position) {
        Uri uri = audios.get(position).getaudioUri();
        File file = new File(uri.getPath());
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
                if (file.exists()) {
                    context.deleteFile(file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.delete()) {
                Toast.makeText(context, "Đã Xóa Thành Công", Toast.LENGTH_SHORT).show();
                if (MusicService.arrayList != null)
                    MusicService.arrayList.remove(arrayList.get(position));

                audios.remove(position);
                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, arrayList.size());
            } else
                Toast.makeText(context, "Xóa Thất Bại", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, "Không tìm thấy file", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, artist;
        ImageView imageView;
        MaterialButton btnOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
            imageView = itemView.findViewById(R.id.image);
            btnOptions = itemView.findViewById(R.id.btn_options_baihat);
        }
    }

}
