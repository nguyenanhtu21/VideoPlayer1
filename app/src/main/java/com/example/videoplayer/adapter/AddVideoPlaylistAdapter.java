package com.example.videoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.MainActivity;
import com.example.videoplayer.R;
import com.example.videoplayer.model.Playlist;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;

public class AddVideoPlaylistAdapter extends RecyclerView.Adapter<AddVideoPlaylistAdapter.PlaylistViewHolder> {
    Context context;
    ArrayList<Playlist> playlists;
    private PlaylistClickListener playlistClickListener;

    public interface PlaylistClickListener {
        void onPlaylistClick(int position);
    }

    public void setPlaylistClickListener(PlaylistClickListener playlistClickListener) {
        this.playlistClickListener = playlistClickListener;
    }

    public AddVideoPlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public AddVideoPlaylistAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_add, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddVideoPlaylistAdapter.PlaylistViewHolder holder, int position) {
        holder.playlist_name.setText(playlists.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlistClickListener != null) {
                    playlistClickListener.onPlaylistClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlist_name;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.play_list_name);
        }
    }
}

