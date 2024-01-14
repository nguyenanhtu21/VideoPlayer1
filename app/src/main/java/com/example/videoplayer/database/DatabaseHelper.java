package com.example.videoplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.videoplayer.model.Playlist;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "videoplayer.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public class PlaylistTable {
        public static final String TABLE_NAME = "playlist";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT)";
    }

    public class VideoTable {
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_TITLE + " TEXT)";
    }

    public class Playlist_Video {
        public static final String TABLE_NAME = "playlist_video";
        public static final String COLUMN_PLAYLIST_ID = "playlist_id";
        public static final String COLUMN_VIDEO_ID = "video_id";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_PLAYLIST_ID + " INTERGER,"
                        + COLUMN_VIDEO_ID + " INTERGER,"
                        + "FOREIGN KEY (" + COLUMN_PLAYLIST_ID + ") REFERENCES " + PlaylistTable.TABLE_NAME + "(" + PlaylistTable.COLUMN_ID + "),"
                        + "FOREIGN KEY (" + COLUMN_VIDEO_ID + ") REFERENCES " + VideoTable.TABLE_NAME + "(" + VideoTable.COLUMN_ID + "),"
                        + "PRIMARY KEY (" + COLUMN_PLAYLIST_ID + ", " + COLUMN_VIDEO_ID + ")"
                        + ")";
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlaylistTable.CREATE_TABLE);
        db.execSQL(VideoTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long addPlaylist(String playlistName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlaylistTable.COLUMN_NAME, playlistName);
        long playlistId = db.insert(PlaylistTable.TABLE_NAME, null, values);
        db.close();
        return playlistId;
    }

    public ArrayList<Playlist> getAllPlaylists(Context context) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM playlist";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String playlistId = cursor.getString(0);
                String playlistName = cursor.getString(1);
                Playlist playlist = new Playlist(playlistName, playlistId, null);
                playlists.add(playlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return playlists;
    }
}
