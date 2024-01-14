package com.example.videoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.R;
import com.example.videoplayer.model.Playlist;

import java.util.ArrayList;

public class AddVideoPlaylistAdapter extends RecyclerView.Adapter<AddVideoPlaylistAdapter.PlaylistViewHolder> {
    Context context;
    ArrayList<Playlist> playlists;

    public AddVideoPlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public AddVideoPlaylistAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_add, parent,false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddVideoPlaylistAdapter.PlaylistViewHolder holder, int position) {
        holder.playlist_name.setText(playlists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder{
        TextView playlist_name;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.play_list_name);
        }
    }
}
