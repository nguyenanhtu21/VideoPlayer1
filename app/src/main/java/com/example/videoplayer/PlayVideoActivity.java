package com.example.videoplayer;

import static com.example.videoplayer.adapter.VideoAdapter.videoList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayVideoActivity extends AppCompatActivity {
    private VideoView videoView;
    int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videoView = findViewById(R.id.videoView);
        position = getIntent().getIntExtra("p", -1);
        String path = videoList.get(position).getPath();
        MediaController controller = new MediaController(this);
        if(path!=null){
            videoView.setVideoPath(path);
            videoView.setMediaController(controller);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
        }else{
            Toast.makeText(this, "Path doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }
}
