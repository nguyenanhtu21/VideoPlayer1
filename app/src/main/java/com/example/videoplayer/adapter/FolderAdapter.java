package com.example.videoplayer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.R;
import com.example.videoplayer.VideoFolderActivity;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    Context context;
    ArrayList<String> folderName;
    ArrayList<Video> videos;

    public FolderAdapter(Context context, ArrayList<String> folderName, ArrayList<Video> videos) {
        this.context = context;
        this.folderName = folderName;
        this.videos = videos;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_layout,parent,false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        int index = folderName.get(position).lastIndexOf("/");
        String folder_name = folderName.get(position).substring(index+1);
        holder.folder_name.setText(folder_name);
        int numberOfVideo = 0;
        for (Video video: videos){
            if(video.getPath().substring(0,video.getPath().lastIndexOf("/")).endsWith(folder_name)){
                numberOfVideo++;
            }
        }
        holder.number_of_videos.setText(String.valueOf(numberOfVideo)+" videos");
        String name  = folderName.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFolderActivity.class);
                intent.putExtra("folderName", name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderName.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folder_name, number_of_videos;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folder_name = itemView.findViewById(R.id.folder_name);
            number_of_videos = itemView.findViewById(R.id.number_of_videos);
        }
    }
}
