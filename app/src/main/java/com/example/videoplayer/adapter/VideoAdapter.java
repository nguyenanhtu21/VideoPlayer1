package com.example.videoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoplayer.PlayVideoActivity;
import com.example.videoplayer.R;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    ArrayList<Video> videoList;
    Context context;

    public VideoAdapter(ArrayList<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.video_title.setText(video.getTitle());
        String size = videoList.get(position).getSize();
        double milliSeconds = Double.parseDouble(videoList.get(position).getDuration());
        holder.video_size.setText(android.text.format.Formatter.formatFileSize(context, Long.parseLong(size)));
        holder.video_duration.setText(timeConvert((long) milliSeconds));
        Glide.with(holder.itemView.getContext())
                .load(video.getThumbnail())
                .placeholder(R.drawable.video_icon)
                .centerCrop()
                .into(holder.video_thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView video_title, video_size, video_duration;
        ImageView video_thumbnail, menu_btn;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            video_title = itemView.findViewById(R.id.video_title);
            video_size = itemView.findViewById(R.id.video_size);
            video_thumbnail = itemView.findViewById(R.id.thumbnail);
            menu_btn = itemView.findViewById(R.id.menu_button);
            video_duration = itemView.findViewById(R.id.videoDuration);
        }
    }

    public String timeConvert(long value){
        String videoDuration;
        long duration = (long)value;
        long hours = (duration/3600000);
        long minutes = (duration/60000)%60000;
        long seconds = (duration/60000)%1000;
        if (hours>0){
            videoDuration = String.format("%02d:%02d:%02d",hours,minutes,seconds);
        }else if(minutes>0){
            videoDuration = String.format("%02d:%02d",minutes,seconds);
        }else{
            videoDuration = String.format("00:%02d",seconds);
        }
        return videoDuration;
    }
}

