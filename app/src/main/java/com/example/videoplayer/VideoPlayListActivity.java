package com.example.videoplayer;

import static com.example.videoplayer.adapter.PlaylistAdapter.playlists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.videoplayer.adapter.VideoAdapter;
import com.example.videoplayer.database.DatabaseHelper;
import com.example.videoplayer.model.Playlist;
import com.example.videoplayer.model.Video;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VideoPlayListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    VideoAdapter adapter;
    ArrayList<Video> videos;
    FloatingActionButton button_play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_list);
        int positon = getIntent().getIntExtra("position", -1);
        Playlist playlist = playlists.get(positon);
        recyclerView = findViewById(R.id.video_playlist_recyclerView);
        DatabaseHelper databaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 2);
        videos = databaseHelper.getPlaylist(playlist.getName());
        adapter = new VideoAdapter(videos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
        button_play = findViewById(R.id.btn_play);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPlayListActivity.this, PlayPlaylistActivity.class);
                intent.putExtra("position", positon);
                startActivity(intent);
            }
        });
    }
}