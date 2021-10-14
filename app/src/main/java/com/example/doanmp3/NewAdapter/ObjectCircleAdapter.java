package com.example.doanmp3.NewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.NewModel.Object;
import com.example.doanmp3.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObjectCircleAdapter extends  RecyclerView.Adapter<ObjectCircleAdapter.ViewHolder> {

    Context context;
    ArrayList<Object> objects;
    ItemClick itemClick;

    public ObjectCircleAdapter(Context context, ArrayList<Object> objects, ItemClick itemClick) {
        this.context = context;
        this.objects = objects;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_object_circle_image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object object = objects.get(position);
        holder.txtName.setText(object.getName());
        Glide.with(context).load(object.getThumbnail()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> itemClick.objectClick(position));
    }

    @Override
    public int getItemCount() {
        if(objects != null)
            return  objects.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView thumbnail;
        TextView txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_object_circle);
            txtName = itemView.findViewById(R.id.name_item_object_circle);
        }
    }

    public interface ItemClick{
        void objectClick(int position);
    }
}
