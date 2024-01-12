package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.videoplayer.adapter.VideoAdapter;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;

public class VideoFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String foler_name;
    ArrayList<Video> videos = new ArrayList<>();
    VideoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        foler_name = getIntent().getStringExtra("folderName");
        videos = getVideoFromFolder(this, foler_name);
        recyclerView = findViewById(R.id.video_recyclerview);
        adapter = new VideoAdapter(videos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Video> getVideoFromFolder(Context context, String folderName) {
        ArrayList<Video> videoArrayList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION
        };

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                String path = cursor.getString(1);
                long id = cursor.getLong(2);
                Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                String size = cursor.getString(3);
                String duration = cursor.getString(4);
                String thumbnailUri = videoUri.toString();
                String idString = String.valueOf(id);
                Video video = new Video(idString, title, path, thumbnailUri, size, duration);
                videoArrayList.add(video);
            }
            cursor.close();
        }
        return videoArrayList;
    }

}