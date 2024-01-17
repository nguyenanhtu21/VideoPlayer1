package com.example.videoplayer;

import static com.example.videoplayer.adapter.PlaylistAdapter.playlists;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.videoplayer.database.DatabaseHelper;
import com.example.videoplayer.model.Playlist;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;

public class PlayPlaylistActivity extends AppCompatActivity {
    VideoView videoView;
    ArrayList<Video> videos;
    private int currentVideoIndex = 0;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_playlist);
        videoView = findViewById(R.id.playlist_videoView);
        int positon = getIntent().getIntExtra("position", -1);
        Playlist playlist = playlists.get(positon);
        DatabaseHelper databaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 2);
        videos = databaseHelper.getPlaylist(playlist.getName());
        playVideo(currentVideoIndex);
    }
    private void playVideo(int index) {
        if (index >= 0 && index < videos.size()) {
            videoView.setVideoPath(videos.get(index).getPath());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer = mp;
                    mediaPlayer.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playNextVideo();
                }
            });
        }
    }

    private void playNextVideo() {
        currentVideoIndex++;
        if (currentVideoIndex < videos.size()) {
            videoView.setVideoPath(videos.get(currentVideoIndex).getPath());
            videoView.start();
        }
    }
}