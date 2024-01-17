package com.example.videoplayer.fragment.folder;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.videoplayer.R;
import com.example.videoplayer.adapter.FolderAdapter;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;

public class FolderFragment extends Fragment {
    ArrayList<String> folderList = new ArrayList<>();
    ArrayList<Video> videoList = new ArrayList<>();
    FolderAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        recyclerView = view.findViewById(R.id.folderRecyclerView);
        videoList = fetchAllVideo(getContext());
        adapter = new FolderAdapter(getContext(),folderList,videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();

        return view;
    }

    ArrayList<Video> fetchAllVideo(Context context){
        ArrayList<Video> videos = new ArrayList<>();
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
        };

        Cursor cursor = contentResolver.query(uri, projection, null,null, null);

        if(cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Uri thumbnailuri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String thumbnailUri = thumbnailuri.toString();
                String idString;
                idString = String.valueOf(id);
                Video video = new Video(idString,title, path, thumbnailUri, size, duration);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);

                if(!folderList.contains(subString)){
                    folderList.add(subString);
                }
                videos.add(video);
            }
            cursor.close();
        }
        return videos;
    }
}