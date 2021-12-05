package com.example.doanmp3.Activity.SystemActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Adapter.CommentAdapter;
import com.example.doanmp3.Models.Comment;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends BaseActivity {


    Toolbar toolbar;
    RecyclerView rvComment;
    CircleImageView imgUser;
    TextInputEditText edtComment;
    MaterialButton btnSend;
    LinearLayout layoutNoComment;

    String typeComment;
    String idObject;
    String nameObject;
    FirebaseUser user;
    DatabaseReference commentRef;
    ChildEventListener childEventListener;
    ArrayList<Comment> comments;
    CommentAdapter adapter;
    boolean isCommentEmpty;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        InitControls();
        GetIntent();
        SetToolBar();
        InitData();
        InitRv();
        ListenEventFromFirebase();
        HandleEvents();
        UpdateTimeComment();
    }

    private void InitControls() {
        toolbar = findViewById(R.id.tool_bar_comment);
        rvComment = findViewById(R.id.rv_comment);
        imgUser = findViewById(R.id.img_user);
        edtComment = findViewById(R.id.edt_comment);
        btnSend = findViewById(R.id.btn_send_comment);
        layoutNoComment = findViewById(R.id.layout_no_comment);
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("type")) {
            typeComment = intent.getStringExtra("type");
        } else {
            typeComment = "songs";
        }

        if (intent != null && intent.hasExtra("idObject")) {
            idObject = intent.getStringExtra("idObject");
        } else {
            idObject = "1";
        }
        if (intent != null && intent.hasExtra("nameObject")) {
            nameObject = intent.getStringExtra("nameObject");
        } else {
            nameObject = "Undefined";
        }
    }

    private void SetToolBar() {
        toolbar.setTitle(nameObject);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void InitData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        commentRef = FirebaseDatabase.getInstance().getReference("comments").child(typeComment).child(idObject);
        comments = new ArrayList<>();
        String photoUrl = user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString();
        Glide.with(this).load(photoUrl).error(R.drawable.person).into(imgUser);
    }

    private void InitRv() {
        adapter = new CommentAdapter(CommentActivity.this, comments, user, this::LoveComment);
        rvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvComment.setAdapter(adapter);
    }

    private void ListenEventFromFirebase() {
        isCommentEmpty = true;
        childEventListener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment = snapshot.getValue(Comment.class);
                if (comment == null) return;
                comments.add(0, comment);
                adapter.notifyDataSetChanged();
                if(isCommentEmpty){
                    rvComment.setVisibility(View.VISIBLE);
                    layoutNoComment.setVisibility(View.GONE);
                    isCommentEmpty = false;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment = snapshot.getValue(Comment.class);
                adapter.onItemChange(comment);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        commentRef.addChildEventListener(childEventListener);
    }

    private void HandleEvents() {
        btnSend.setOnClickListener(v -> {
            String comment = Objects.requireNonNull(edtComment.getText()).toString().trim();
            if (comment.equals("") || comment.length() < 5) {
                Toast.makeText(CommentActivity.this, R.string.comment_min_length, Toast.LENGTH_SHORT).show();
            } else
                PostComment(comment);
        });

    }

    private void PostComment(String comment) {
        String key = commentRef.push().getKey();
        if (key != null) {
            String photoUrl = user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString();
            Comment newComment = new Comment(key, user.getUid(), user.getDisplayName(), photoUrl, comment);
            commentRef.child(key).setValue(newComment);
        }
        edtComment.setText("");
        Tools.hideSoftKeyBoard(this);
        rvComment.smoothScrollToPosition(0);
    }

    private void UpdateTimeComment() {
        handler = new Handler();
        runnable = new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (comments != null && comments.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 1000 * 60);
            }
        };

        handler.postDelayed(runnable, 1000 * 60);
    }

    private void LoveComment(int position) {
        Comment comment = comments.get(position);
        List<String> likes = comment.getLiked();
        if(likes == null){
            likes = new ArrayList<>();
            likes.add(user.getUid());
        }else{
            if(likes.contains(user.getUid())){
                likes.remove(user.getUid());
            }else{
                likes.add(user.getUid());
            }
        }
        commentRef.child(comment.getId()).child("liked").setValue(likes);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        commentRef.removeEventListener(childEventListener);
    }
}