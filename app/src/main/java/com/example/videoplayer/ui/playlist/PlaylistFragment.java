package com.example.videoplayer.ui.playlist;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.R;
import com.example.videoplayer.adapter.PlaylistAdapter;
import com.example.videoplayer.database.DatabaseHelper;
import com.example.videoplayer.databinding.FragmentPlaylistBinding;
import com.example.videoplayer.model.Playlist;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {
    RecyclerView recyclerView;
    PlaylistAdapter adapter;
    ArrayList<Playlist> playlistsArraylist;

    public static final String DATABASE_NAME = "videoplayer.db";
    DatabaseHelper dbHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, 1);
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = view.findViewById(R.id.playlistRecyclerView);
        playlistsArraylist = dbHelper.getAllPlaylists(getContext());
        adapter = new PlaylistAdapter(getContext(), playlistsArraylist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
        ImageView addPlaylist = view.findViewById(R.id.addPlaylist);
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPlaylist();
            }
        });
        return view;
    }
    public void addNewPlaylist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add playlist");

        final EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);

        editText.requestFocus();

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                try {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.PlaylistTable.COLUMN_NAME, editText.getText().toString().trim());

                    long playlistId = db.insert(DatabaseHelper.PlaylistTable.TABLE_NAME, null, values);

                    if (playlistId != -1) {
                        Toast.makeText(getContext(),"Added", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                }
            }
        }).create().show();
    }

}