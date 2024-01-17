package com.example.videoplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.R;
import com.example.videoplayer.VideoPlayListActivity;
import com.example.videoplayer.database.DatabaseHelper;
import com.example.videoplayer.model.Playlist;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    Context context;
    public static ArrayList<Playlist> playlists;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public PlaylistAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.play_list_layout, parent,false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.playlist_name.setText(playlists.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayListActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
        holder.menu_btn.setOnClickListener(new View.OnClickListener() {
            DatabaseHelper dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, 2);
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.menu_btn);
                popupMenu.inflate(R.menu.playlist_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete_item) {
                            dbHelper.deletePlaylist(playlist.getId());
                            Toast.makeText(context, "Đã xóa danh sách phát "+playlist.getName(), Toast.LENGTH_SHORT).show();
                            return true;
                        } else if(item.getItemId()==R.id.rename_item){
                            renamePlaylist(playlist);
                            return true;
                        }else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void renamePlaylist(Playlist playlist){
        DatabaseHelper dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, 2);
        AlertDialog.Builder builder  =new AlertDialog.Builder(context);
        builder.setTitle("Đổi tên danh sách phát");
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText(playlist.getName());
        builder.setView(editText);
        editText.requestFocus();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.renamePlaylist(playlist.getId(),editText.getText().toString());
                Toast.makeText(context,"Đổi tên thành công", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }
    public class PlaylistViewHolder extends RecyclerView.ViewHolder{
        ImageView menu_btn;
        TextView playlist_name;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.play_list_name);
            menu_btn = itemView.findViewById(R.id.menu_button);
        }
    }
}
