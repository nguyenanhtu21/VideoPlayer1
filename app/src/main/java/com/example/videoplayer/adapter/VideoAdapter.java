package com.example.videoplayer.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoplayer.PlayVideoActivity;
import com.example.videoplayer.R;
import com.example.videoplayer.database.DatabaseHelper;
import com.example.videoplayer.model.Playlist;
import com.example.videoplayer.model.Video;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    public static ArrayList<Video> videoList;
    final Context context;

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
        holder.video_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("p", holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
        String filePath = videoList.get(position).getPath();
        holder.menu_btn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.menu_btn);
            popupMenu.inflate(R.menu.video_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.rename_item) {
                        renameVideo(filePath);
                        return true;
                    } else if (item.getItemId() == R.id.delete_item) {
                        deleteVieo(filePath);
                        return true;
                    }else if (item.getItemId()==R.id.add_to_playlist_item){
                        AddVideoPlaylistAdapter adapter;
                        DatabaseHelper dbHelper = new DatabaseHelper(context, "videoplayer.db", null, 1);
                        ArrayList<Playlist> playlists = dbHelper.getAllPlaylists(context);
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.playlist_bottom_sheet,
                                v.findViewById(R.id.bottom_sheet));
                        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.playlist_recyclerView);
                        adapter = new AddVideoPlaylistAdapter(context, playlists);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
            popupMenu.show();
        });
    }
    public  void addVideoToPlaylist(Video video){

    }

    public void renameVideo(String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đổi tên");
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        final File file = new File(filePath);
        String videoName = file.getName();
        videoName = videoName.substring(0, videoName.lastIndexOf("."));
        editText.setText(videoName);
        builder.setView(editText);
        editText.requestFocus();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String onlyPath = file.getParentFile().getAbsolutePath();
                String extension = file.getAbsolutePath();
                extension = extension.substring(extension.lastIndexOf("."));
                String newPath  = onlyPath  +"/"+ editText.getText().toString() + extension;
                File newFile = new File(newPath);

                boolean rename = file.renameTo(newFile);
                if(rename){
                    context.getApplicationContext().getContentResolver().
                            delete(MediaStore.Files.getContentUri("external"),
                            MediaStore.MediaColumns.DATA+"=?",
                            new String[]{file.getAbsolutePath()});
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(newFile));
                    context.getApplicationContext().sendBroadcast(intent);
                    notifyDataSetChanged();
                    Toast.makeText(context,"Đã đổi tên",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"Đổi tên không thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.create().show();
    }
    public void deleteVieo(String filePath){
        final File file = new File(filePath);
        context.getApplicationContext().getContentResolver().
                delete(MediaStore.Files.getContentUri("external"),
                        MediaStore.MediaColumns.DATA+"=?",
                        new String[]{file.getAbsolutePath()});
        notifyDataSetChanged();
        Toast.makeText(context,"Đã xóa",Toast.LENGTH_SHORT).show();
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


    public String timeConvert(long value) {
        String videoDuration;
        long duration = value;
        long hours = (duration / 3600000);
        long minutes = (duration / 60000) % 60;
        long seconds = (duration / 1000) % 60;

        if (hours >= 1) {
            videoDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else if (minutes >= 1) {
            videoDuration = String.format("%02d:%02d", minutes, seconds);
        } else {
            videoDuration = String.format("00:%02d", seconds);
        }

        return videoDuration;
    }

}

