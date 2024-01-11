package com.example.videoplayer.ui.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.R;
import com.example.videoplayer.adapter.VideoAdapter;
import com.example.videoplayer.databinding.FragmentLocalBinding;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Video> videoList = new ArrayList<>();
    VideoAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local,container,false);
        recyclerView = view.findViewById(R.id.videoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(videoList, getContext());
        recyclerView.setAdapter(adapter);
        videoList.clear();

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String [] projection = {MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};

        Cursor cursor = contentResolver.query(videoUri, projection, null,null, null);

        if(cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String thumbnailUri = uri.toString();
                Video video = new Video(title, path, thumbnailUri, size, duration);
                videoList.add(video);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}